import React, { Component } from 'react';
import './App.css';
import FeeForm from "./FeeForm"

class App extends Component {
    constructor() {
        super();
        this.state = {};
    };

    render(){
        return (
           <FeeForm/>
        );
    }
}

export default App;
