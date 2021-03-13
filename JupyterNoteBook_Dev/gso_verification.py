from lxml import etree as ET
from datetime import datetime
import pandas as pd
import os
from collections import Counter
import yaml

# Global Dict Initialization
SHOW_WARN = True
dict_configs = {}

dict_cs_mapping = {}
dict_gs_mapping = {}
dict_cs_occ_fields = {}
dict_gs_occ_fields = {}

# Lookup Dictionaries
dict_cs_lookups = {}
dict_gs_lookups = {}


# Convert lxml dict to standard dict
def convert_lxml_to_dict(lxml_dict):
    dict_out = {}
    for key in lxml_dict:
        dict_out[key] = lxml_dict[key]
    return dict_out


# ------------------------------------- All Dictionary Methods --------------------------------------------#

# Lookup Dictionary Method
# Return 3 dict
# Primary Key : Attributes of Element
# Unique Keys : Attributes of Element
# Primary Key : Actual Element
def get_element_dict3(tree, elm_name, unique_columns, dict_lookup, id_col='id'):
    dict_element_id = {}
    dict_element_unique_col = {}
    dict_tree_element = {}
    for sub_element in tree.findall(".//" + elm_name):
        element_attrib = sub_element.attrib
        dict_attrib = convert_lxml_to_dict(element_attrib)
        if (elm_name == 'part'):  # Special Handling for Participant to get definition
            dict_attrib['definition'] = dict_lookup['part'][dict_attrib[id_col]] if dict_attrib[id_col] in dict_lookup[
                'part'] else dict_lookup['part']
        dict_element_id[dict_attrib[id_col]] = dict_attrib
        unique_key = '-'.join([dict_attrib[col] for col in unique_columns if col in dict_attrib])
        dict_element_unique_col[unique_key] = dict_attrib
        dict_tree_element[dict_attrib[id_col]] = sub_element
    return dict_element_id, dict_element_unique_col, dict_tree_element


# Lookup Dictionary Method
# Return Definition Key : Attributes of Element
def get_gso_config_dict(tree, dict_lookups):
    dict_config = {}
    for gso_elm in tree.findall('.//businessEntity'):
        for config_part_elm in gso_elm.findall('.//configurations/configuration/part'):
            dict_attrib = {}
            dict_attrib['gso'] = gso_elm.attrib['name']
            dict_attrib['option'] = config_part_elm.getparent().attrib['option']
            dict_attrib['definition'] = dict_lookups['part'][config_part_elm.attrib['groupPartId']]
            dict_attrib.update(convert_lxml_to_dict(config_part_elm.attrib))
            dict_config[dict_attrib['definition']] = dict_attrib
            dict_attrib['gso_namespace'] = gso_elm.attrib['nameSpace']
    return dict_config


# Lookup Dictionary Method
# Return Primary Key : Attributes of Element
def get_lookup_dict(tree, elm_name, id_col='id', name_col='name'):
    dict_lookup = {}
    for sub_element in tree.findall(".//" + elm_name):
        element_attrib = sub_element.attrib
        dict_lookup[element_attrib.get(id_col)] = convert_lxml_to_dict(element_attrib)[name_col]
    return dict_lookup


# Lookup Dictionary Method
# Return DGDP Primary Key : Attributes of Definition Element
# Example DGDP_ID : Attributes of BFDF_OID by doing lookup in BFDF Dictionary
def get_lookup_part_dict(tree, dict_lookups):
    dict_lookup = {}
    for sub_element in tree.findall('.//dataGroup/part'):
        element_attrib = sub_element.attrib
        dict_elm = convert_lxml_to_dict(element_attrib)
        if ('beRelationId' in dict_elm):
            dict_lookup[dict_elm['id']] = 'REL-' + dict_lookups['relation'][dict_elm['beRelationId']]
        elif ('dataGroupId' in dict_elm):
            dict_lookup[dict_elm['id']] = 'DG-' + dict_lookups['dataGroup'][dict_elm['dataGroupId']]
        elif ('fieldId' in dict_elm):
            dict_lookup[dict_elm['id']] = 'FIELD-' + dict_lookups['field'][dict_elm['fieldId']]
    return dict_lookup


# Lookup Dictionary Method
# Return OCFP Primary Key : Attributes of Field
def get_occ_field_dict(tree):
    dict_element = {}
    for sub_element in tree.findall('.//occurrence'):
        for elm in sub_element.findall('field'):
            element_attrib = elm.attrib
            dict_attr = convert_lxml_to_dict(element_attrib)
            dict_attr['occ_id'] = sub_element.attrib.get('id')
            dict_attr['occ_name'] = sub_element.attrib.get('name')
            if ('name' not in dict_attr):
                dict_attr['name'] = dict_attr['relationId']
            dict_element[element_attrib.get('id')] = dict_attr
    return dict_element


# Lookup Dictionary Method
# Return BFDF Primary Key : Attributes of Mapping Element
def get_mapping_dict(tree):
    dict_element_mapping = {}
    for sub_element in tree.findall('.//mapping'):
        element_attrib = sub_element.attrib
        if (element_attrib.get('beFieldId') not in dict_element_mapping.keys()):
            dict_element_mapping[element_attrib.get('beFieldId')] = []
        dict_element_mapping[element_attrib.get('beFieldId')].append(element_attrib.get('occFieldId'))
    return dict_element_mapping


# Lookup Dictionary Method
# Dict of all lookup dictionaries
def get_all_lookups(tree):
    dict_lookups = {}
    dict_lookups['type'] = get_lookup_dict(tree, 'entiyType')
    dict_lookups['gso'] = get_lookup_dict(tree, 'businessEntity')
    dict_lookups['field'] = get_lookup_dict(tree, 'beField')
    dict_lookups['dataGroup'] = get_lookup_dict(tree, 'dataGroup')
    dict_lookups['relation'] = get_lookup_dict(tree, 'entityRelation')
    dict_lookups['part'] = get_lookup_part_dict(tree, dict_lookups)
    return dict_lookups;


# ------------------------------------- All Dictionary Methods --------------------------------------------#

# Verification Method - Specific
# Verify only mapping
def verify_mapping(dict_result, key):
    global dict_cs_mapping
    global dict_gs_mapping
    global dict_cs_occ_fields
    global dict_gs_occ_fields
    global SHOW_WARN

    # Checking mappings
    # mapping list for comparison
    mapping_cs = dict_cs_mapping[key] if key in dict_cs_mapping.keys() else []
    mapping_gs = dict_gs_mapping[key] if key in dict_gs_mapping.keys() else []
    if (Counter(set(mapping_cs)) != Counter(set(mapping_gs))) and SHOW_WARN:
        dict_result['VERIFICATION_CODE'] = 'WARN_MAPING_MISMATCH'
        dict_result['DIFF_FIRST'] = ','.join([dict_cs_occ_fields[val]['occ_name'] + '.' + dict_cs_occ_fields[val]['name']
                                           for val in mapping_cs if val in dict_cs_occ_fields])
        dict_result['DIFF_SECOND'] = ','.join([dict_gs_occ_fields[val]['occ_name'] + '.' + dict_gs_occ_fields[val]['name']
                                           for val in mapping_gs if val in dict_gs_occ_fields])


# Verification Method - Main
def verify_element(dict_config, tree_elm_cs, tree_elm_gs, dict_parent=None):
    global dict_configs
    global dict_cs_lookups
    global dict_gs_lookups
    global SHOW_WARN

    elm_name = dict_config['name']
    list_comp_cols = dict_config['compare_cols']
    list_unique_cols = dict_config['unique_cols']
    list_display_cols = dict_config['display_cols']
    id_col = dict_config['id_col'] if 'id_col' in dict_config else 'id'
    verify_addon = dict_config['verify_addon'] if 'verify_addon' in dict_config else None
    list_child_element = dict_config['child_elements'] if 'child_elements' in dict_config else None

    dict_cs_elm_id, dict_cs_elm_unique, dict_cs_tree_elm = get_element_dict3(tree_elm_cs, elm_name, list_unique_cols,
                                                                             dict_cs_lookups, id_col=id_col)
    dict_gs_elm_id, dict_gs_elm_unique, dict_gs_tree_elm = get_element_dict3(tree_elm_gs, elm_name, list_unique_cols,
                                                                             dict_gs_lookups, id_col=id_col)

    list_result = []
    for key in dict_cs_elm_id:
        # CS sub dict for comparison
        dict_attr_cs = dict_cs_elm_id[key]
        dict_attr_cmp_cs = {key: dict_attr_cs[key] for key in dict_attr_cs if key in list_comp_cols}

        # Unique column value
        key_unique = '-'.join([dict_attr_cs[col] for col in list_unique_cols if col in dict_attr_cs])

        # Prepare result dict
        dict_result = {}
        for col in list_display_cols:
            if ('->' in col):
                lookup_source = col.split('->')[0]
                lookup_value = dict_attr_cs[col.split('->')[1]]
                dict_result[lookup_source.upper()] = dict_cs_lookups[lookup_source][lookup_value]
            else:
                if (col in dict_attr_cs):
                    col_name = elm_name.upper() + '_' + col.upper() if elm_name in ['field', 'value',
                                                                                    'option'] else col.upper()
                    dict_result[col_name] = dict_attr_cs[col]

        # Result from parent
        if (dict_parent != None):
            dict_extra = {key: dict_parent[key] for key in dict_parent if
                          key not in ['VERIFICATION_CODE', 'DIFF_FIRST', 'DIFF_SECOND', 'CROSS_REF_ID']
                          and key not in dict_result.keys()}
            dict_result.update(dict_extra)

        VERIFICATION_CODE = None
        list_child_result = []

        if (key in dict_gs_elm_id.keys()):
            # If key matched

            # GS sub dict for comparison
            dict_attr_gs = dict_gs_elm_id[key]
            dict_attr_cmp_gs = {key: dict_attr_gs[key] for key in dict_attr_gs if key in list_comp_cols}
            dict_diff = dict(set(dict_attr_cmp_cs.items()) - set(dict_attr_cmp_gs.items()))

            # Checking field attributes
            if (len(dict_diff) > 0) and SHOW_WARN:
                dict_result['VERIFICATION_CODE'] = 'WARN_DATA_MISMATCH'
                dict_result['DIFF_FIRST'] = ', '.join(
                    [k + ' = ' + dict_attr_cmp_cs[k] for k in dict_diff if k in dict_attr_cmp_cs])
                dict_result['DIFF_SECOND'] = ', '.join(
                    [k + ' = ' + dict_attr_cmp_gs[k] for k in dict_diff if k in dict_attr_cmp_gs])

            # Addon Verification
            if (verify_addon != None and verify_addon != 'None'):
                str_exec = verify_addon + '(dict_result,key)'
                eval(str_exec)

            # Verify Child Elements
            if (list_child_element != None):
                for child_elm_name in list_child_element:
                    list_fetch = [config for config in dict_configs['elements'] if config['name'] == child_elm_name]
                    if (len(list_fetch) == 1):
                        dict_fetch = list_fetch[0]
                        dict_child_config = {key: dict_fetch[key] for key in dict_fetch}
                        dict_child_config['name'] = child_elm_name.replace(elm_name + '.', '')
                        list_tmp = verify_element(dict_child_config, dict_cs_tree_elm[key], dict_gs_tree_elm[key],
                                                  dict_result)
                        list_child_result.extend(list_tmp)
                    else:
                        # print(list_fetch)
                        print(child_elm_name + ' element not configured')

        else:
            # Checking uniquness for OOB field
            if (key_unique in dict_gs_elm_unique.keys()):
                dict_attr_gs = dict_gs_elm_unique[key_unique]
                ns1 = dict_attr_cs.get('defNameSpace') if 'defNameSpace' in dict_attr_cs else dict_attr_cs.get(
                    'nameSpace')
                ns2 = dict_attr_gs.get('defNameSpace') if 'defNameSpace' in dict_attr_gs else dict_attr_cs.get(
                    'nameSpace')
                dict_result['VERIFICATION_CODE'] = 'FATAL_ID_NAME_CONFLICT' if (ns1 != ns2) else 'ERR_ID_NAME_CONFLICT'
                dict_result['CROSS_REF_ID'] = 'Cross ref id : ' + dict_gs_elm_unique[key_unique]['id']
            else:
                if SHOW_WARN:
                    dict_result['VERIFICATION_CODE'] = 'WARN_EXTRA_ELEMENT'

        # Update Result
        if ('VERIFICATION_CODE' in dict_result.keys()):
            list_result.append(dict_result)
            if (len(list_child_result)):
                list_result.extend(list_child_result)

    return list_result


# Verification Method - Specific
# Verify only Configuration
def verify_config_element(dict_config, tree_elm_cs, tree_elm_gs):
    global dict_cs_lookups
    global dict_gs_lookups
    global SHOW_WARN

    elm_name = dict_config['name']
    list_comp_cols = dict_config['compare_cols']
    list_display_cols = dict_config['display_cols']

    dict_cs_config_elm = get_gso_config_dict(tree_elm_cs, dict_cs_lookups)
    dict_gs_config_elm = get_gso_config_dict(tree_elm_gs, dict_gs_lookups)

    list_result = []
    for key in dict_cs_config_elm:
        # CS sub dict for comparison
        dict_attr_cs = dict_cs_config_elm[key]
        dict_attr_cmp_cs = {key: dict_attr_cs[key] for key in dict_attr_cs if key in list_comp_cols}

        # Prepare result dict
        dict_result = {col.upper(): dict_attr_cs[col] for col in list_display_cols if (col in dict_attr_cs)}

        VERIFICATION_CODE = None
        ## For Configuration need to check only OOB GSO
        if (dict_attr_cs['gso_namespace'] == 'GSC'):
            if (key in dict_gs_config_elm.keys()):
                # If key matched
                # GS sub dict for comparison
                dict_attr_gs = dict_gs_config_elm[key]
                dict_attr_cmp_gs = {key: dict_attr_gs[key] for key in dict_attr_gs if key in list_comp_cols}
                dict_diff = dict(set(dict_attr_cmp_cs.items()) - set(dict_attr_cmp_gs.items()))

                # Checking field attributes
                if (len(dict_diff) > 0) and SHOW_WARN:
                    dict_result['VERIFICATION_CODE'] = 'WARN_DATA_MISMATCH'
                    dict_result['DIFF_FIRST'] = ', '.join(
                        [k + ' = ' + dict_attr_cmp_cs[k] for k in dict_diff if k in dict_attr_cmp_cs])
                    dict_result['DIFF_SECOND'] = ', '.join(
                        [k + ' = ' + dict_attr_cmp_gs[k] for k in dict_diff if k in dict_attr_cmp_gs])

            else:
                if SHOW_WARN :
                    dict_result['VERIFICATION_CODE'] = 'WARN_DATA_MISMATCH'
                    dict_result['DIFF_FIRST'] = 'Found'

        # Update Result
        if ('VERIFICATION_CODE' in dict_result.keys()):
            list_result.append(dict_result)

    if SHOW_WARN:
        for key in dict_gs_config_elm:
            # GS sub dict for comparison
            dict_attr_gs = dict_gs_config_elm[key]

            # Prepare result dict
            dict_result = {col.upper(): dict_attr_gs[col] for col in list_display_cols if (col in dict_attr_gs)}

            if (key not in dict_cs_config_elm.keys()):
                dict_result['VERIFICATION_CODE'] = 'WARN_DATA_MISMATCH'
                dict_result['DIFF_SECOND'] = 'Found'

            # Update Result
            if ('VERIFICATION_CODE' in dict_result.keys()):
                list_result.append(dict_result)
    return list_result


# Main Emtry Point

def verify_gso(file_config, file_name_cs, file_name_gs, dir_out,show_warning=True):
    tree_cs = ET.parse(file_name_cs)
    tree_gs = ET.parse(file_name_gs)

    global dict_configs
    global dict_cs_mapping
    global dict_gs_mapping
    global dict_cs_occ_fields
    global dict_gs_occ_fields
    global dict_cs_lookups
    global dict_gs_lookups
    global SHOW_WARN

    SHOW_WARN = show_warning
    print(SHOW_WARN)
    # Specific Dict Initialization
    dict_cs_mapping = get_mapping_dict(tree_cs)
    dict_gs_mapping = get_mapping_dict(tree_gs)
    dict_cs_occ_fields = get_occ_field_dict(tree_cs)
    dict_gs_occ_fields = get_occ_field_dict(tree_gs)

    # Lookup Dictionaries
    dict_cs_lookups = get_all_lookups(tree_cs)
    dict_gs_lookups = get_all_lookups(tree_gs)

    stream = open(file_config, 'r')
    dict_configs = yaml.safe_load(stream)

    list_summary = []
    dict_summary = {}
    dict_result = {}
    for dict_config in dict_configs['elements']:
        if (dict_config['parent'] == 'Y'):
            if (dict_config['name'] == 'config'):
                list_result = verify_config_element(dict_config, tree_cs, tree_gs)
            else:
                list_result = verify_element(dict_config, tree_cs, tree_gs)

            ## Persisiting Result
            df_out = pd.DataFrame(list_result)
            df_out.reset_index(drop=True, inplace=True)
            df_out.to_csv(os.path.join(dir_out, dict_config['name'] + '_conflict.csv'), index=False)
            if (df_out is not None):
                list_summary.append({'Element': dict_config['name'], 'No. of Conflicts': len(df_out)})
                dict_result[dict_config['name']] = df_out

    df_summary = pd.DataFrame(list_summary)
    return df_summary