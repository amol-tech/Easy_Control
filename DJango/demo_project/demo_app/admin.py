from django.contrib import admin
from demo_app.models import Department,Employee,UserProfileInfo

# Register your models here.
admin.site.register(Department)
admin.site.register(Employee)
admin.site.register(UserProfileInfo)