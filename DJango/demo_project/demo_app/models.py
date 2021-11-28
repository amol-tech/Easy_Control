from django.db import models

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
