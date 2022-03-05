import dash
import dash_html_components as html
import dash_core_components as dcc

app = dash.Dash()
app.layout = html.Div(children=[html.Frameset([html.Frame(id='A'),html.Frame(id='B')])])

if __name__ == '__main__':
    app.run_server(debug=True)