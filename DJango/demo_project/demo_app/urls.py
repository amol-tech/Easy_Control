from django.conf.urls import url
from demo_app import views
from django.urls import path

app_name = 'demo_app'

urlpatterns = [
    path('show/', views.index2,name='index'),
    path('show_nav/', views.index_nav,name='index_navigation'),
    path('show_other/', views.index_other,name='other'),
    path('register_user/', views.register_user, name='register_user'),
    path('login/', views.login_user, name='login'),
    path('logout/', views.logout_user,name='logout'),
    path('special/', views.special,name='special'),
    path('show_car/', views.index_car,name='index_car'),
    path('show_employee/', views.index_employee, name='index_employee'),
    path('entry_emp/', views.index_form_submit, name='index_form_submit'),
    path('entry_welcome/', views.index_welcome, name='index_welcome')
]
