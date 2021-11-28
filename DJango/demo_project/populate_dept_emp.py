import os
from faker import Faker
import random
os.environ.setdefault('DJANGO_SETTINGS_MODULE','demo_project.settings')

import django
django.setup()

from demo_app.models import Department,Employee

fake_gen = Faker()
designations = ['Manager','Officer','Clerk','Salesman']

def add_department(name,location):
    dept = Department.objects.get_or_create(dname=name,location=location)[0]
    dept.save()

    for entry in range(20):
        emp = Employee.objects.get_or_create(department=dept,
                                             ename=fake_gen.name(),
                                             desig=random.choice(designations),
                                             salary=fake_gen.pydecimal(left_digits=6,right_digits=0))[0]
        print(emp)
        emp.save()
    return dept

if __name__ == '__main__':
    print('Population Started !!')
    #print(random.choice(designations))
    add_department('Sales','Banaglore')
    print('Population Complete !!')


