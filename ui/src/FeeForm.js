import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import * as Constants from "./constants"
import CalculatorForm from "./CalculatorForm"

class FeeForm extends Component {
    constructor(props) {
        super(props);

        this.state = { 
            currencyList:[],
            currencyPairs:[],
            baseCurrency: "EUR",
            quoteCurrency: "USD",
            feeAmount: 0
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleRemove = this.handleRemove.bind(this);
        this.switchToCalculatorForm = this.switchToCalculatorForm.bind(this);
    }

    componentDidMount() {
        fetch(Constants.API + 'currencies')
            .then(response => response.json())
            .then(data => this.setState({ currencyList: data.result }));
        fetch(Constants.API + 'pairs')
            .then(response => response.json())
            .then(data => this.setState({ currencyPairs: data.result }));  
    }

    handleChange(event) {
        //TODO add validation for number 
        const target = event.target;
        this.setState({
            [target.name]: target.value
        });
    }

    handleSubmit(event) {
        let request = {
            pairName: this.state.baseCurrency + this.state.quoteCurrency,
            feeAmount: this.state.feeAmount
        };
        this.setFeeAndRefreshPairs(request);
        event.preventDefault();
    }

    handleRemove(event, pairName) {
        let request = {
            pairName: pairName,
            feeAmount: 0
        };
        this.setFeeAndRefreshPairs(request);
        event.preventDefault();
    }

    setFeeAndRefreshPairs(request){
        request.reqId = Math.floor(Math.random() * 1000000000);
        console.log(request);
        fetch(Constants.API + 'fees',{
            method: 'PUT',
            body: JSON.stringify(request),
            cache: "no-cache",
            headers: {
                "Content-type": "application/json; charset=UTF-8"
            }
        })
        .then(response => response.json())
        .then(data => {
            console.log(data.result);
            if (data.result == "success"){
                fetch(Constants.API + 'pairs')
                    .then(response => response.json())
                    .then(data => this.setState({ currencyPairs: data.result }));  
            }}
        );
    }

    switchToCalculatorForm(event){
        ReactDOM.render(<CalculatorForm/>, document.querySelector('#app_container'));
    }

  render() {
    //TOOD pagination
    return (
    <div className="container">
        <form className="w-50">
            <div className="form-row align-items-end">
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
                <div className="col-md-3">
                    <label className="col-form-label col-form-label-sm">Fee</label>
                    <input type="text" className="form-control form-control-sm"
                        name="feeAmount" 
                        value={this.state.feeAmount} 
                        onChange={this.handleChange} />
                </div>
                <div className="col-md-2">
                    <input type="submit" onClick={this.handleSubmit} className="btn btn-sm btn-info" value="Add"/>
                </div>
                <div className="col-md-1">
                    <button className="btn btn-sm btn-success" onClick={() => this.switchToCalculatorForm()}>Calculator</button>
                </div>
            </div>
        </form>
    
        <hr/>

        {this.state.currencyPairs.map((pair, i) => 
            <div className="row p-1" key={i}>
                <div className="col-md-2">{pair.name}</div>
                <div className="col-md-2">{pair.base}</div>
                <div className="col-md-2">{pair.quote}</div>
                <div className="col-md-2">{pair.fee}</div>
                <div className="col-md-2">
                    <button className="btn btn-sm btn-danger" onClick={(event) => this.handleRemove(event, pair.name)}>Remove</button>
                </div>
            </div>
        )}
       
    </div>
     
   );
  }
}

export default FeeForm;