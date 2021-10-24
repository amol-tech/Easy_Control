from django.shortcuts import render
from django.http import HttpResponse

# Create your views here.
def index2(request):
    my_dict = {'data_var':'Here i am going to display !'}
    return render(request,'demo_app/hello_world.html',context=my_dict)