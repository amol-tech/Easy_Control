from django import forms
from demo_app.models import Employee,UserProfileInfo
from django.contrib.auth.models import User

class EmployeeForm(forms.ModelForm):
    class Meta():
        model = Employee
        fields = '__all__'

    '''
    name = forms.CharField()
    desig = forms.CharField()
    salary = forms.DecimalField()
    doj = forms.DateField()
    botcatcher = forms.CharField(required=False,widget=forms.HiddenInput)

    def clean_botcathcer(self):
        botcatcher = self.cleaned_data['botcatcher']
        if len(botcatcher) > 0:
            raise forms.ValidationError('Caught BOT!!')
        return botcatcher
    '''

class UserForm(forms.ModelForm):
    password = forms.CharField(widget=forms.PasswordInput())

    class Meta:
        model = User
        fields = ('username','email','password')

class UserProfileInfoForm(forms.ModelForm):
    portfolio = forms.URLField(required=False)
    picture = forms.ImageField(required=False)

    class Meta():
        model = UserProfileInfo
        fields = ('portfolio','picture')