from django.conf.urls import url
from demo_app import views
from django.urls import path,re_path

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
    path('entry_welcome/', views.index_welcome, name='index_welcome'),
    path('show_cbv/', views.CBView.as_view(), name='index_cbv'),
    path('show_school/', views.SchoolIndexView.as_view(), name='index_school'),
    path('list_school/', views.SchoolListView.as_view(), name='list_school'),
    path('create_school/', views.SchoolCreateView.as_view(), name='create_school'),
    re_path(r'^list_school/(?P<pk>\d+)/$', views.SchoolDetailView.as_view(), name='detail_school'),
    re_path(r'^update_school/(?P<pk>\d+)/$', views.SchoolUpdateView.as_view(), name='update_school'),
    re_path(r'^delete_school/(?P<pk>\d+)/$', views.SchoolDeleteView.as_view(), name='delete_school')
]
