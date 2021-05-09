from lxml import etree as ET
import os
import sys

# To get the signature of method name from tsd file Ex. SetEngineConfiguration[2][string,string]
def get_method_name(elm_method):
    dataTypes = '['+ ','.join([elm_para.attrib['dataType'].lower() for elm_para in elm_method.findall('.//Parameter')])+']'
    return elm_method.attrib['methodName'] + '[' + str(len(elm_method.findall('.//Parameter'))) +']'+dataTypes

# To get the signature of method name from java doc 
def get_method_impl_name(elm_step):
    dataTypes = '['+ ','.join([elm_para.attrib['format'].lower() for elm_para in elm_step.findall('.//UiParameter')])+']'
    return elm_step.attrib['stepName'] + '[' + str(len(elm_step.findall('.//UiParameter'))) +']'+dataTypes

# Main method to convert Test Definition
def convert(file_jdoc,file_tsd):
    if not(os.path.exists(file_jdoc)):
        print('File not found '+file_jdoc)
        return
       
    if not(os.path.exists(file_tsd)):
        print('File not found '+file_tsd)
        return
    
    tree_jdoc = ET.parse(file_jdoc)
    tree_tsd = ET.parse(file_tsd)
    # Dict of all method signature and actual element
    dict_method_elm  = {get_method_name(elm):elm for elm in tree_jdoc.findall('.//Method')}

    # Iterate all method elemnent in tsd and match the signature with jova doc and if found then set the parameter name as per java doc
    list_missed_method = []
    for elm_step in tree_tsd.findall('.//UiTestStep'):
        method_impl_name = get_method_impl_name(elm_step)
        if(method_impl_name in dict_method_elm):
            list_method_params = dict_method_elm[method_impl_name].findall('.//Parameter')
            list_step_params = elm_step.findall('.//UiParameter')
            for i in range(0,len(list_step_params)):
                elm_m_param = list_method_params[i]
                elm_s_param = list_step_params[i]
                elm_s_param.attrib['name'] = elm_m_param.attrib['parameterName']
        else:
            list_missed_method.append(method_impl_name)
    set_missed_methods = set(list_missed_method)
    
    with open(file_tsd, "wb") as f:
        f.write(ET.tostring(tree_tsd, pretty_print = True))
    print('Test Definition converted successfully!')
    
    if len(set_missed_methods) > 0 :
        with open('converter.log', 'w') as f:
            f.write('Following methods are not present in java doc\n')
            for item in set_missed_methods:
                f.write("%s\n" % item)

# -------------------------------------------------------------------------------------------------------------------
print('First Argument :' + sys.argv[1])
print('Second Argument :' + sys.argv[2])
convert(sys.argv[1],sys.argv[2])

               