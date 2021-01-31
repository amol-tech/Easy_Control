import dash
import dash_html_components as html
import dash_core_components as dcc

app = dash.Dash()
app.layout = html.Div(className='row', children=[
    html.H1("Tips database analysis (First dashboard)"),
    dcc.Dropdown(),
    html.Div(children=[
        dcc.Graph(id="graph1", style={'display': 'inline-block'}),
        dcc.Graph(id="graph2", style={'display': 'inline-block'})
    ])
])

if __name__ == '__main__':
    app.run_server(debug=True)