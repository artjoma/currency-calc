'use strict';

const API = 'api/adm/v1/';
const domContainer = document.querySelector('#app_container');

class FeeForm extends React.Component {
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
        fetch(API + 'currencies')
            .then(response => response.json())
            .then(data => this.setState({ currencyList: data.result }));
        fetch(API + 'pairs')
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
        fetch(API + 'fees',{
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
                fetch(API + 'pairs')
                    .then(response => response.json())
                    .then(data => this.setState({ currencyPairs: data.result }));  
            }}
        );
    }

    switchToCalculatorForm(event){
        ReactDOM.render(<CalculatorForm/>, domContainer);
    }

  render() {
    //TOOD pagination
    return (
    <div class="container">
        <form class="w-50">
            <div class="form-row align-items-end">
                <div class="col-md-3">
                    <label for="baseCurrency" class="col-form-label col-form-label-sm">From</label>
                    <select class="form-control form-control-sm" 
                        name="baseCurrency"  
                        value={this.state.baseCurrency}
                        onChange={this.handleChange}>    
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                        </select>
                </div>
                <div class="col-md-3">
                    <label for="quoteCurrency" class="col-form-label col-form-label-sm">To</label>
                    <select class="form-control form-control-sm"
                        name="quoteCurrency"
                        value={this.state.quoteCurrency} 
                        onChange={this.handleChange}>
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="fee" class="col-form-label col-form-label-sm">Fee</label>
                    <input type="text" class="form-control form-control-sm"
                        name="feeAmount" 
                        value={this.state.feeAmount} 
                        onChange={this.handleChange} />
                </div>
                <div class="col-md-2">
                    <input type="submit" onClick={this.handleSubmit} class="btn btn-sm btn-info" value="Add"/>
                </div>
                <div class="col-md-1">
                    <button class="btn btn-sm btn-success" onClick={() => this.switchToCalculatorForm()}>Calculator</button>
                </div>
            </div>
        </form>
    
        <hr/>

        {this.state.currencyPairs.map((pair) => 
            <div class="row p-1">
                <div class="col-md-2">{pair.name}</div>
                <div class="col-md-2">{pair.base}</div>
                <div class="col-md-2">{pair.quote}</div>
                <div class="col-md-2">{pair.fee}</div>
                <div class="col-md-2">
                    <button class="btn btn-sm btn-danger" onClick={() => this.handleRemove(event, pair.name)}>Remove</button>
                </div>
            </div>
        )}
       
    </div>
     
   );
  }
}

class CalculatorForm extends React.Component {
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
        fetch(API + 'currencies')
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
                fetch(API + 'conversion',{
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
        ReactDOM.render(<FeeForm/>, domContainer);
    }

  render() {
    //TOOD pagination
    return (
    <div class="container">
        <form class="w-50">
            <div class="form-row align-items-end">
                <div class="col-md-3">
                    <label for="fee" class="col-form-label col-form-label-sm">Amount</label>
                    <input type="text" class="form-control form-control-sm"
                        name="amount" 
                        value={this.state.amount} 
                        onChange={this.handleChange} />
                </div>
                <div class="col-md-3">
                    <label for="baseCurrency" class="col-form-label col-form-label-sm">From</label>
                    <select class="form-control form-control-sm" 
                        name="baseCurrency"  
                        value={this.state.baseCurrency}
                        onChange={this.handleChange}>    
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                        </select>
                </div>
                <div class="col-md-3">
                    <label for="quoteCurrency" class="col-form-label col-form-label-sm">To</label>
                    <select class="form-control form-control-sm"
                        name="quoteCurrency"
                        value={this.state.quoteCurrency} 
                        onChange={this.handleChange}>
                        {this.state.currencyList.map((val) => <option key={val} value={val}>{val}</option>)}
                    </select>
                </div>
                <div class="col-md-3 text-right">
                    <button class="btn btn-success btn-sm" role="button" onClick={() => this.switchToFeeForm()}>Fees</button>
                </div>
            </div>
        </form>
        
        <h3>{this.state.result}</h3>
    </div>
     
   );
  }
}

ReactDOM.render(<FeeForm/>, domContainer);
