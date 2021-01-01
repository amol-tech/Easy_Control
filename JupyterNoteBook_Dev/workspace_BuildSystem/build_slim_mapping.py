from lxml import etree as ET
import os.path
from os import path
import shutil
import zipfile
import tarfile
import sys

# -------------------------------------------------------------------------------------------------------------------
'''
Helper Function's

    get_package_tar_name_and_version - to get the package tar name and version by parsing package description
    make_tarfile - to make tar file of given directory
    set_package_name - set the package name in package description xml

'''

def get_package_tar_name_and_version(file_package_desc):
    tree = ET.parse(file_package_desc)
    str_package_file_name = [elm_file.attrib['path'] for elm_file in tree.findall('.//Content/File') 
                             if elm_file.attrib['type'] == 'Package'][0]
    str_package_version = [elm_package.attrib['version'] for elm_package in tree.findall('.//Package')][0] 
    return str_package_file_name,str_package_version

def make_tarfile(output_filename, dir_source,exclude_parent=False):
    with tarfile.open(output_filename, "w:gz") as tar:
        if(exclude_parent):
            for f in os.listdir(dir_source):
                file_path = os.path.join(dir_source,f)
                tar.add(file_path,arcname=os.path.basename(file_path))
        else:
            tar.add(dir_source,arcname=os.path.basename(dir_source))
            
def set_package_name(file_package_desc,package_name):
    tree = ET.parse(file_package_desc)
    tree.find('Package').attrib['name'] = package_name
    root = tree.getroot()    
    tree.write(file_package_desc)

def get_fields(tree,field_set_id):
    fields = []
    list_field_set  = [field_set for field_set in tree.findall('.//field_set') if field_set.attrib['id'] == field_set_id]
    elm_field_set = list_field_set[0] if len(list_field_set) > 0 else None
    if (elm_field_set != None) :
        fields = [elm_field.attrib['name']+"=>"+elm_field.attrib['type'] for elm_field in elm_field_set.findall('field')]
    return fields

# -------------------------------------------------------------------------------------------------------------------
'''
	Function : split_config
    Read configuration xml file and split into multiple text files of fields
'''
def split_config(file_config,dir_out,dir_mapping):
    dict_config = {}
    with open(file_config, 'r') as content_file:
        content = content_file.read()
    dict_fields = {}
    tree = ET.fromstring(content)
    for elm_pkg in tree.findall('.//package'):
        list_config_part = []
        for elm_file in elm_pkg.findall('file'):
            dict_config_part = {}
            dict_config_part['mdx'] = os.path.join(dir_mapping, elm_file.attrib['name'])
            
            file_name = elm_pkg.attrib['name']+ '_' + elm_file.attrib['name'].replace('.mdx','').replace('*','all')+'_fields.txt'
            list_fields = get_fields(elm_pkg,elm_file.attrib['field_set'])
            if(len(list_fields) > 0) :
                str_fields = '\n'.join(list_fields)
                dict_fields[file_name] = str_fields
                dict_config_part['config'] = os.path.join(dir_out, file_name)
            else:
                dict_config_part['config'] = None
            list_config_part.append(dict_config_part)
        dict_config[elm_pkg.attrib['name']]=list_config_part 
        
    for file_name in dict_fields:
        path = os.path.join(dir_out, file_name)
        with open(path, 'w') as f:
            f.write(dict_fields[file_name])
        print('Processed file '+file_name)
    return dict_config
# -------------------------------------------------------------------------------------------------------------------
'''
	Function : extract_package
    Extract given package into directory
'''
def extract_package(file_package,dir_extract):
    if os.path.exists(dir_extract) and os.path.isdir(dir_extract):
        shutil.rmtree(dir_extract)
    os.mkdir(dir_extract) 

    file_package_target = os.path.join(dir_extract,os.path.basename(file_package))
    shutil.copy(file_package,dir_extract)
    with zipfile.ZipFile(file_package_target, 'r') as zip_ref:
        zip_ref.extractall(dir_extract)
        
    # Removing unwanted files 
    os.remove(file_package_target)
    os.remove(os.path.join(dir_extract,'UsedFieldsReports.tar.gz'))
    os.remove(os.path.join(dir_extract,'MappingReports.tar.gz'))
    
    # Creating required directory
    os.mkdir(os.path.join(dir_extract,'UsedFieldsReports'))
    os.mkdir(os.path.join(dir_extract,'MappingReports'))
    
    os.mkdir(os.path.join(dir_extract,'content'))
    file_package_desc = os.path.join(dir_extract,'PackageDescription.xml')
    package_tar_name,package_version = get_package_tar_name_and_version(file_package_desc)
    file_tar = os.path.join(dir_extract,package_tar_name)
    tar = tarfile.open(file_tar)
    tar.extractall(os.path.join(dir_extract,'content')) 
    tar.close()
    print('Package extracted successfully')
# -------------------------------------------------------------------------------------------------------------------
'''
	Function : build_package
    To build the package with the content of given directory and package name
'''
def build(file_build_config,file_package):
    dir_temp = os.path.join(os.getcwd(),'temp')
    dir_mapping = os.path.join(dir_temp,'content'+os.path.sep+'MappingSpecification')
    dir_out = 'output'
    dir_config = 'config'
    dir_dependencies ='dependencies'

    # Creating output directory
    if os.path.exists(dir_out) and os.path.isdir(dir_out):
        shutil.rmtree(dir_out)
    os.mkdir(dir_out)

    # Creating config directory
    if os.path.exists(dir_config) and os.path.isdir(dir_config):
        shutil.rmtree(dir_config)
    os.mkdir(dir_config)

    packages = split_config(file_build_config,dir_config,dir_mapping)
    
    file_cmd = 'generateSlimMappingForFields.cmd'
    for package in packages:
        extract_package(file_package,dir_temp)
        # Copy Dependencies
        copy_dependencies(dir_dependencies,dir_temp)

        for mdx_config in packages[package]:
            if( mdx_config['config'] != None):
                if('*.mdx' in mdx_config['mdx']):
                    for f in os.listdir(dir_mapping):
                        process = subprocess.run([file_cmd,os.path.join(dir_mapping,f), mdx_config['config']], 
                                                 check=True, stdout=subprocess.PIPE,universal_newlines=True, shell=True)
                        print(process)
                else:
                    process = subprocess.run([file_cmd,mdx_config['mdx'], mdx_config['config']], 
                                                 check=True, stdout=subprocess.PIPE,universal_newlines=True, shell=True)
                    print(process)
            print('--------------------------------')   
        # Remove Dependencies
        remove_dependencies

        build_package(dir_temp,dir_out,package)
# -------------------------------------------------------------------------------------------------------------------
'''
	Function : build
    Main calling function

'''
def build(file_build_config,file_package):
    dir_temp = os.path.join(os.getcwd(),'temp')
    dir_mapping = os.path.join(dir_temp,'content'+os.path.sep+'MappingSpecification')
    dir_out = 'output'
    dir_config = 'config'

    # Creating output directory
    if os.path.exists(dir_out) and os.path.isdir(dir_out):
        shutil.rmtree(dir_out)
    os.mkdir(dir_out)

    # Creating config directory
    if os.path.exists(dir_config) and os.path.isdir(dir_config):
        shutil.rmtree(dir_config)
    os.mkdir(dir_config)

    packages = split_config(file_build_config,dir_config,dir_mapping)

    for package in packages:
        extract_package(file_package,dir_temp)
        for mdx_config in packages[package]:
            if('*.mdx' in mdx_config['mdx']):
                for f in os.listdir(dir_mapping):
                    print(os.path.join(dir_mapping,f))
            else:
                print(mdx_config['mdx'])
            print('--------------------------------')      
        build_package(dir_temp,dir_out,package)

# -------------------------------------------------------------------------------------------------------------------
print('First Argument :' + sys.argv[1])
print('Second Argument :' + sys.argv[2])
build(sys.argv[1],sys.argv[2])

