from django import forms
from demo_app.models import Employee

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