from django.db import models
from django.contrib.auth.models import User
from django.urls import reverse

# Create your models here.

class Department(models.Model):
    dname = models.CharField(max_length=255,unique=True)
    location = models.CharField(max_length=255)

    def __str__(self):
        return self.dname

class Employee(models.Model):
    department = models.ForeignKey(Department,on_delete=models.DO_NOTHING)
    ename = models.CharField(max_length=255,unique=True)
    desig = models.CharField(max_length=255)
    salary = models.DecimalField(max_digits=12,decimal_places=2)
    doj = models.DateField(default=None,null=True)

    def __str__(self):
        return self.ename


class UserProfileInfo(models.Model):

    # Relationship with User
    user = models.OneToOneField(User,on_delete=models.DO_NOTHING)

    # Additional Attribute
    portfolio = models.URLField(blank=True)
    picture = models.ImageField(upload_to='profile_pics',blank=True)

    def __str__(self):
        return self.user.username

class School(models.Model):
    name = models.CharField(max_length=256)
    principal = models.CharField(max_length=256)
    location = models.CharField(max_length=256)
    def __str__(self):
        return self.name

    def get_absolute_url(self):
        return reverse("demo_app:detail_school",kwargs={'pk':self.pk})


class Student(models.Model):
    name = models.CharField(max_length=256)
    age = models.PositiveIntegerField()
    school = models.ForeignKey(School,related_name='students',on_delete=models.DO_NOTHING)

    def __str__(self):
        return self.name