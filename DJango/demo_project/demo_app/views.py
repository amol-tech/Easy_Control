from django.shortcuts import render
from django.http import HttpResponse
from demo_app.models import Department,Employee
from . import forms

# Create your views here.
def index2(request):
    my_dict = {'data_var':'Here i am going to display !'}
    return render(request,'demo_app/hello_world.html',context=my_dict)

def index_car(request):
    return render(request,'demo_app/car_intro.html')

def index_employee(request):
    list_emp = Employee.objects.order_by('ename')
    dict_emp = {'emp_records':list_emp}
    return render(request,'demo_app/employee_info.html',context=dict_emp)

def index_form(request):
    form_emp = forms.EmployeeForm()
    dict_form = {'form_emp': form_emp}

    if request.method == 'POST':
        form_emp = forms.EmployeeForm(request.POST)
        if form_emp.is_valid():
            print('Validation Succeed')
            print('Name' + form_emp.cleaned_data['name'])
            print('Designation' + form_emp.cleaned_data['desig'])
            print('Salary' + str(form_emp.cleaned_data['salary']))
            print('Joining Date' + str(form_emp.cleaned_data['doj']))

    return render(request,'demo_app/form_emp.html',context=dict_form)

def index_welcome(request):
    return render(request, 'demo_app/emp_welcome.html')

def index_form_submit(request):
    form_emp = forms.EmployeeForm()
    dict_form = {'form_emp': form_emp}

    if request.method == 'POST':
        form_emp = forms.EmployeeForm(request.POST)
        if form_emp.is_valid():
            form_emp.save(commit=True)
            return index_welcome(request)
        else:
            print('Invalid Entry!!')

    return render(request, 'demo_app/form_emp.html', context=dict_form)