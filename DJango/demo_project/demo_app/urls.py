from django.conf.urls import url
from demo_app import views
from django.urls import path

app_name = 'basic_app'

urlpatterns = [
    path('show/', views.index2,name='index'),
    path('show_car/', views.index_car,name='index_car'),
    path('show_employee/', views.index_employee, name='index_employee'),
    path('entry_emp/', views.index_form_submit, name='index_form_submit'),
    path('entry_welcome/', views.index_welcome, name='index_welcome')
]
