import ipywidgets as widgets
from ipytree import Tree, Node
import re
import math
import time
import threading

'''
    Custom Widget class which allow user to add or delete option from Combobox to Select widget
'''
class MulitSelect():

    # Widgets used
    __w_combo_search = None
    __w_select_list = None
    __w_button_add = None
    __w_button_del = None
    __w_vbox_widget = None

    # keep the value variable as standard output to return final selections as tuple
    value = ()

    ID_PATH = None
    INITIALIZED = False

    def __init__(self,layout,ID_PATH,show_combo=False):
        self._render(layout,show_combo)
        self.__w_combo_search.INITIALIZED = False
        self.__w_combo_search.ID_PATH = ID_PATH

    def _render(self,layout,show_combo):
        if show_combo:
            self.__w_combo_search = widgets.Combobox(layout={'width': '70%'})
        else:
            self.__w_combo_search = widgets.Dropdown(layout={'width': '70%'})
        self.__w_button_add = widgets.Button(description='+',layout={'width': '15%'})
        self.__w_button_del = widgets.Button(description='X', layout={'width': '15%'})
        w_hbox = widgets.HBox([self.__w_combo_search,self.__w_button_add,self.__w_button_del],layout={'width': 'auto','height': '40px','overflow': 'hidden'})
        self.__w_select_list = widgets.Select(layout={'width': 'auto','height':'auto'})
        self.__w_vbox_widget = widgets.VBox([w_hbox,self.__w_select_list],layout=layout)
        self.__w_button_add.on_click(self._add_option)
        self.__w_button_del.on_click(self._del_option)

    def get_container(self):
        return self.__w_vbox_widget

    def observe(self,handler):
        self.__w_combo_search.observe(handler,names='value')

    def set_options(self,options):
        options.insert(0,'')
        self.__w_combo_search.options = options

    def set_values(self,values):
        self.__w_select_list.options = values
        self.value = values

    def _add_option(self,button_obj):
        self.__w_select_list.options += self.__w_combo_search.value,
        self.__w_combo_search.value = ''
        self.value = self.__w_select_list.options

    def _del_option(self,button_obj):
        list_options = list(self.__w_select_list.options)
        list_options.remove(self.__w_select_list.value)
        self.__w_select_list.options = list_options
        self.value = self.__w_select_list.options


'''
    Custom Tree widget to create hierarchical view for given list of GSO fields
'''
class GSOFieldTreeControl():
    # Widgets used
    __w_text_search = None
    __w_tree_fields = None
    __w_vbox_widget = None
    __w_button_search = None
    __w_button_clear = None
    __list_fields = []
    __list_filter_fields = []
    __list_participant = []
    __list_root_participant = []

    def __init__(self):
        self.__render()

    def get_container(self):
        return self.__w_vbox_widget

    def set_input(self,list_fields):
        self.__list_fields = list_fields
        self.__list_filter_fields = [field for field in self.__list_fields]
        self.__transform_data()
        self.__fill_data()

    # To render tree control
    def __render(self):
        self.__w_text_search = widgets.Text(placeholder='Press enter to search GSO Field',layout={'height':'auto','width':'100%'})
        self.__w_button_search = widgets.Button(icon='fa-search', layout={'width': '35px'},tooltip='Search')
        self.__w_button_clear = widgets.Button(icon='fa-refresh', layout={'width': '35px'},tooltip='Refresh')
        self.__w_text_search.on_submit(self.__on_execute_search)
        self.__w_button_search.on_click(self.__on_execute_search)
        self.__w_button_clear.on_click(self.__on_execute_clear)
        w_hbox_search = widgets.HBox([self.__w_text_search,self.__w_button_search,self.__w_button_clear],layout={'height':'7%','width':'auto'})
        self.__w_tree_fields = Tree(layout={'height':'2000px','width':'100%'})
        w_vbox_border = widgets.VBox([self.__w_tree_fields],
                                     layout={'height':'93%','width':'100%','ovrflow_y': 'auto','display': 'block'})
        self.__w_vbox_widget = widgets.VBox([w_hbox_search,w_vbox_border],
                                            layout={'border': '1px solid gray','height':'100%','width':'100%'})
    # -------------------------------------------------------------------

    # Fill data from Model Objects to UI Objects
    def __fill_data(self,reveal=False,lazy_loading=True):
        self.__w_tree_fields.CHILDREN = self.__list_root_participant
        self.__create_nodes(self.__w_tree_fields,reveal=reveal,lazy_loading=lazy_loading)


    def __create_nodes(self,parent,reveal,lazy_loading=False):
        list_tree_part = parent.CHILDREN
        list_nodes = []

        for tree_part in list_tree_part:
            icon_name = 'circle' if tree_part.is_field else 'table'
            icon_style = 'warning' if tree_part.is_field else 'success'
            node = Node(tree_part.name, icon=icon_name, icon_style=icon_style,opened=reveal)
            list_nodes.append(node)
            node.PATH = tree_part.path
            node.CHILDREN = tree_part.children
            if lazy_loading and not tree_part.is_field:
                node.nodes = [Node('Loading...', icon='circle', icon_style='warning', opened=False)]
                node.observe(self.__handle_opened, 'opened')
                node.LOADED = False

        if parent is not None:
            parent.nodes = list_nodes

        for node in list_nodes:
            if not lazy_loading:
                node.LOADED = True
                self.__create_nodes(node,reveal)
    # -------------------------------------------------------------------

    def __handle_opened(self,event):
        node = event['owner']
        if not node.LOADED:
            self.__create_nodes(node, reveal=False, lazy_loading=True)
        node.LOADED = True

    # Transform flat list of GSO fields to hierarchical Model Objects
    def __transform_data(self):
        self.__list_root_participant.clear()
        self.__list_participant.clear()
        for field in self.__list_filter_fields:
            if '.' in field:
                self.__create_tree_participant(field)

    def __create_tree_participant(self,field):
        parent = None
        path = None
        for part in field.split('.'):
            path = path + '.' + part if path is not None else part
            child = self.__get_participant(part,path,parent)
            if parent != None:
                parent.add_child(child)
            else:
                self.__add_root_particiapnt(child)
            parent = child
        child.is_field = True

    # -------------------------------------------------------------------

    def __on_execute_clear(self, button_obj):
        self.__w_text_search.value = ''
        self.__refresh_tree()

    # Listener method on button search
    def __on_execute_search(self,button_obj):
        self.__refresh_tree()

    def __refresh_tree(self):
        reveal_nodes = True
        lazy_loading = False
        if len(self.__w_text_search.value) <= 1:
            self.__list_filter_fields = [field for field in self.__list_fields]
            reveal_nodes = False
            lazy_loading = True
        else:
            self.__list_filter_fields = [field for field in self.__list_fields if
                                         re.search(self.__w_text_search.value, field, re.IGNORECASE)]
        self.__transform_data()
        self.__fill_data(reveal=reveal_nodes, lazy_loading=lazy_loading)

    def observer(self,handler):
        self.__w_tree_fields.observe(handler,names='selected_nodes')

    # Create or get model object base on value
    def __get_participant(self,name,path,parent):
        tree_part = None
        list_find = [p for p in self.__list_participant if p.path == path]
        if len(list_find) > 0 :
            tree_part = list_find[0]
        else:
            tree_part = TreeParticipant(name,path,parent)
            self.__list_participant.append(tree_part)
        return tree_part

    def get_root_particiapnt(self):
        return self.__list_root_participant

    def __add_root_particiapnt(self,part):
        list_find = [p for p in self.__list_root_participant if p.path == part.path]
        if len(list_find) == 0 :
            self.__list_root_participant.append(part)

    def get_selection(self):
        list_field_selction = [node.PATH for node in self.__w_tree_fields.selected_nodes]
        return list_field_selction

    def reset_selection(self):
        for node in self.__w_tree_fields.selected_nodes:
            node.selected = False

# Model Object Class
class TreeParticipant():
    parent = None
    name = None
    children = []
    path = None
    is_field = False

    def __init__(self,name,path,parent=None):
        self.name = name
        self.path = path
        self.parent = parent
        self.children = []

    def add_child(self,child):
        list_find = [p for p in self.children if p.path == child.path]
        if len(list_find) == 0:
            self.children.append(child)


class GSOFieldTableControl():
    # Widgets used
    __w_table_fields = None
    __w_vbox_widget = None
    __list_fields = []

    def __init__(self):
        self.__render()

    def set_input(self,list_fields):
        self.__list_fields = list_fields
        self.__fill_data()

    def get_container(self):
        return self.__w_vbox_widget

    # To render tree control
    def __render(self):
        self.__w_table_fields = Tree(layout={'height': '2000px', 'width': '100%'})
        self.__w_vbox_widget = widgets.VBox([self.__w_table_fields],
                                            layout={'border': '1px solid gray', 'height': '100%', 'width': '100%'})

    # Fill data from Model Objects to UI Objects
    def __fill_data(self):
        list_nodes = []
        for field in self.__list_fields:
            node = Node(field, icon='circle', icon_style='warning')
            list_nodes.append(node)
        self.__w_table_fields.nodes = list_nodes

    def get_selection(self):
        list_field_selction = [node.name for node in self.__w_table_fields.selected_nodes]
        return list_field_selction[0] if len(list_field_selction) > 0 else None

'''
    Custom Widget class to create vertical tab
'''
from pkg_resources import resource_filename,Requirement

class VTab():
    # Widgets used
    __w_vbox_tabs = None
    __w_vbox_area = None
    __w_hbox_widget = None
    __dict_widgets = {}
    __list_all_buttons = []

    def __init__(self, dict_widgets,height,width,width_percent=85):
        self.__dict_widgets = dict_widgets
        self.__list_all_buttons = []
        self.__render(height,width,width_percent)

    def __render(self,height,width,width_percent):
        box_layout = widgets.Layout(display='flex',
                flex_flow='column',
                align_items='center',
                width='50%')
        self.__w_vbox_tabs = widgets.VBox([], layout={'border': '1px solid gray','height': height, 'width': str(100-width_percent)+'%'})
        self.__w_vbox_area = widgets.VBox([], layout={'border': '1px solid gray','align_items':'center','height': 'auto', 'width': str(width_percent)+'%'})

        self.__w_hbox_widget = widgets.HBox([self.__w_vbox_tabs,self.__w_vbox_area],layout={'width': width})

        for t_name in self.__dict_widgets:
            w_button = widgets.Button(description=t_name, layout={'width': 'auto','display':'flex','justify_content':'flex-start'})
            w_button.CONTROL = self.__dict_widgets[t_name]
            w_button.on_click(self.__on_selection)
            self.__list_all_buttons.append(w_button)

        self.__w_vbox_tabs.children = self.__list_all_buttons

    def __on_selection(self,w_button):
        for b in self.__list_all_buttons:
            b.button_style = ''
        w_button.button_style = 'primary'        
        self.__w_vbox_area.children = [w_button.CONTROL]
        self.__w_vbox_area.layout.align_items='unset'


    def get_container(self):
        return self.__w_hbox_widget


'''
    Custom Widget class to show the dataframe into multiple pages using pagination
'''
class OutputPages():
    # Widgets used
    __w_hbox_pagination = None
    __w_output = None
    __df_data = None
    __page_index = 1
    __w_vbox_widget = None
    __w_output = None
    __page_size = 15
    __w_btn_next = None
    __w_btn_prev = None
    __w_btn_first = None
    __w_btn_last = None

    def __init__(self, df_data,page_size=15):
        self.__page_size = page_size
        self.__df_data = df_data
        self.__render()

    def __render(self):
        btn_dm = '36px'
        self.__w_btn_first = widgets.Button(icon='fa-fast-backward',layout={'width': btn_dm})
        self.__w_btn_prev = widgets.Button(icon='fa-step-backward', layout={'width': btn_dm})
        self.__w_btn_next = widgets.Button(icon='fa-step-forward', layout={'width': btn_dm})
        self.__w_btn_last = widgets.Button(icon='fa-fast-forward', layout={'width': btn_dm})
        self.__w_hbox_pagination = widgets.HBox([self.__w_btn_first,self.__w_btn_prev,self.__w_btn_next,self.__w_btn_last],layout={'display':'flex','height': '36px','justify_content':'flex-end'})
        self.__w_output = widgets.Output()
        w_vbox_output = widgets.HBox([self.__w_output], layout={'border': '1px solid gray','height': 'auto', 'width': 'auto'})


        if  isinstance(self.__df_data,list):
            self.__w_vbox_widget = widgets.VBox([w_vbox_output],layout={'height': '100%', 'width': 'auto'})
            with self.__w_output:
                self.__w_output.clear_output()
                for listelem in self.__df_data:
                    display(listelem)
        else:
            self.__w_vbox_widget = widgets.VBox([self.__w_hbox_pagination,w_vbox_output],layout={'height': '100%', 'width': 'auto'})
            self.__refresh_data()
            self.__refresh_buttons()

            self.__w_btn_prev.on_click(self.__on_click_prev)
            self.__w_btn_next.on_click(self.__on_click_next)
            self.__w_btn_first.on_click(self.__on_click_first)
            self.__w_btn_last.on_click(self.__on_click_last)
            self.__w_btn_prev.disabled = True

    def __refresh_data(self):
        start_index = (self.__page_size * self.__page_index) - self.__page_size
        end_index = (self.__page_size * self.__page_index) if self.__df_data.size >= (self.__page_size * self.__page_index) else self.__df_data.size
        with self.__w_output:
            self.__w_output.clear_output()
            display(self.__df_data[start_index:end_index])

    def __refresh_buttons(self):
        self.__w_btn_next.disabled = True if (self.__page_size * (self.__page_index)) > self.__df_data.shape[0] else False
        self.__w_btn_prev.disabled = True if self.__page_index == 1 else False
        self.__w_btn_first.disabled = True if self.__page_index == 1 else False
        self.__w_btn_last.disabled = True if (self.__page_size * (self.__page_index)) > self.__df_data.shape[0] else False

    def __on_click_next(self,w_button):
        self.__page_index = self.__page_index + 1
        self.__refresh_data()
        self.__refresh_buttons()

    def __on_click_prev(self,w_button):
        self.__page_index = self.__page_index - 1
        self.__refresh_data()
        self.__refresh_buttons()

    def __on_click_first(self,w_button):
        self.__page_index = 1
        self.__refresh_data()
        self.__refresh_buttons()

    def __on_click_last(self,w_button):
        self.__page_index = math.ceil(self.__df_data.shape[0]/self.__page_size)
        self.__refresh_data()
        self.__refresh_buttons()

    def get_container(self):
        return self.__w_vbox_widget


'''
    Custom Widget class to show the output in multiple tabs
'''
class OutputResult():
    # Widgets used
    __list_output = list()
    __w_tab = None

    def __init__(self, list_output):
        self.__list_output = list_output
        self.__render()

    def __render(self):
       # self.__w_tab = widgets.Tab(layout={'height':'100%'})
        self.__w_tab = widgets.Tab()
        tab_children = []
        i = 0
        for dict_out in self.__list_output:            
            if 'name' in dict_out and 'data' in dict_out :
                if isinstance(dict_out['data'],str):
                    # If no data found message
                    w_text_message = widgets.Textarea(dict_out['data'],layout={'height':'400px','width':'100%'})
                    w_text_message.disabled = True
                    tab_children.append(widgets.VBox([w_text_message]))
                else:
                    w_out_result = OutputPages(dict_out['data'])
                    tab_children.append(widgets.VBox([w_out_result.get_container()]))
                i = i + 1
            if 'name' in dict_out and 'dynamic_graph' in dict_out:    
                fig=dict_out.get('dynamic_graph')
                image_widget = widgets.Output()
                with image_widget:
                    display(fig.show()) 
                tab_children.append(image_widget)   
                i = i + 1   
            if 'name' in dict_out and 'graph' in dict_out:                
                if isinstance(dict_out.get('graph'), str):
                    file = open(dict_out.get('graph'), "rb")
                    image = file.read()
                elif isinstance(dict_out.get('graph'), list):
                    img_list=list()
                    for path in dict_out.get('graph'):
                        file = open(path, "rb")
                        image = file.read()
                        image_widget = widgets.Image(value=image, format='png', width=800, height=700,)
                        img_list.append(image_widget)
                    tab_children.append(widgets.VBox(img_list))   
                else:
                    image_widget = widgets.Output()
                    with image_widget:
                        display(dict_out.get('graph'))        
                i = i + 1    
        self.__w_tab.children = tab_children
        
        # Setting the title
        i = 0
        for dict_out in self.__list_output: 
            self.__w_tab.set_title(i, dict_out['name'])
            i = i + 1

    def get_container(self):
        return self.__w_tab

'''
    Custom Widget class to show the busy progress bar when attached listner method executed.
'''
class ProgressBar():
    __w_progres = None
    __width = None
    __halt = False

    def __init__(self, width):
        self.__width = width
        self.__render()

    def __render(self):
        self.__w_progres = widgets.IntProgress(value=0, min=0, max=10,layout={'width': self.__width})

    def __work(self):
        while not(self.__halt):
            count = 0
            while count <= 10:
                self.__w_progres.value = count  # signal to increment the progress bar
                time.sleep(.1)
                count += 1
        self.__w_progres.style.bar_color = "green"

    def stop(self):
        self.__halt = True

    def start(self):
        self.__halt = False
        thread = threading.Thread(target=self.__work)
        thread.start()

    def get_container(self):
        return self.__w_progres
'''Widget to support showing PDF in a cell,can be used for documentation and Help section'''    
class PDF(object):
    def __init__(self, pdf, size=(200,200)):
        self.pdf = pdf
        self.size = size

    def _repr_html_(self):
        return '<iframe src={0} width={1[0]} height={1[1]}></iframe>'.format(self.pdf, self.size)

    def _repr_latex_(self):
        return r'\includegraphics[width=1.0\textwidth]{{{0}}}'.format(self.pdf)

'''VBox widget along with heading label '''
class SectionBox():
    # Widgets used
    __v_box_section = None
    __w_line = None
    __w_label = None
    __layout = None
    __title = None
    __children = None

    def __init__(self, title,layout,children):
        self.__title = title
        self.__layout = layout
        self.__children = children
        self.__render()

    def __render(self):
        self.__w_line = widgets.VBox(layout={'border': '1px solid gray', 'height': '1px', 'width': '95%'})
        self.__w_label = widgets.Label(self.__title)
        v_box_area = widgets.VBox(self.__children,layout={'height': '100%', 'width': 'auto', 'margin': '5px 0px 0px 0px'})
        self.__v_box_section = widgets.VBox([self.__w_label, self.__w_line, v_box_area], layout=self.__layout)

    def get_container(self):
        return self.__v_box_section