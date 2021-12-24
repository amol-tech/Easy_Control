from django.contrib import admin
from demo_app.models import Department,Employee,UserProfileInfo,School,Student

# Register your models here.
admin.site.register(Department)
admin.site.register(Employee)
admin.site.register(UserProfileInfo)
admin.site.register(School)
admin.site.register(Student)
