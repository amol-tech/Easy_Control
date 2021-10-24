from django.conf.urls import url
from demo_app import views
from django.urls import path

urlpatterns = [
    path('', views.index2,name='index'),
]
