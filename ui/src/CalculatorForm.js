import React, { Component } from 'react';
import * as Constants from "./constants"
import ReactDOM from 'react-dom';
import FeeForm from "./FeeForm"

class CalculatorForm extends Component {
    constructor(props) {
        super(props);

        this.state = { 
            currencyList: [],
            baseCurrency: "EUR",
            quoteCurrency: "USD",
            amount: 0,
            result: 0
        };

        this.handleChange = this.handleChange.bind(this);
        this.switchToFeeForm = this.switchToFeeForm.bind(this);
    }

    componentDidMount() {
        fetch(Constants.API + 'currencies')
            .then(response => response.json())
            .then(data => this.setState({ currencyList: data.result }));
    }

    handleChange(event) {
        const target = event.target;
        //setState works async!
        this.setState({
            [target.name]: target.value
        }, () => {
            if (this.state.amount > 0){
                let request = {
                    pairName: this.state.baseCurrency + this.state.quoteCurrency,
                    amount: this.state.amount,
                    reqId: Math.floor(Math.random() * 1000000000)
                };
                console.log(request);
                fetch(Constants.API + 'conversion',{
                    method: 'POST',
                    body: JSON.stringify(request),
                    cache: "no-cache",
                    headers: {
                        "Content-type": "application/json; charset=UTF-8"
                    }
                })
                .then(response => response.json())
                .then(data => {
                        console.log(data.result.amount);
                        this.setState({result: data.result.amount});
                    }
                );
                event.preventDefault();
            }
        });
        
    }

    switchToFeeForm(){
        ReactDOM.render(<FeeForm/>, document.querySelector('#app_container'));
    }

  render() {
    //TOOD pagination
    return (
    <div className="container">
        <form className="w-50">
            <div className="form-row align-items-end">
                <div className="col-md-3">
                    <label className="col-form-label col-form-label-sm">Amount</label>
                    <input type="text" className="form-control form-control-sm"
                        name="amount" 
                        value={this.state.amount} 
                        onChange={this.handleChange} />
                </div>
                <div className="col-md-3">
                    <label className="col-form-label col-form-label-sm">From</label>
                    <select className="form-control form-control-sm" 
                        name="baseCurrency"  
                        value={this.state.baseCurrency}
                        onChange={this.handleChange}>    
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                        </select>
                </div>
                <div className="col-md-3">
                    <label className="col-form-label col-form-label-sm">To</label>
                    <select className="form-control form-control-sm"
                        name="quoteCurrency"
                        value={this.state.quoteCurrency} 
                        onChange={this.handleChange}>
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                    </select>
                </div>
                <div className="col-md-3 text-right">
                    <button className="btn btn-success btn-sm" role="button" onClick={() => this.switchToFeeForm()}>Fees</button>
                </div>
            </div>
        </form>
        
        <h3>{this.state.result}</h3>
    </div>
     
   );
  }
}

export default CalculatorForm;