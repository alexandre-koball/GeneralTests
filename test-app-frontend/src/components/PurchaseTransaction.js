import React, { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Autocomplete, Button, Container, Paper } from '@mui/material';
import '../scroll.css';

export default function PurchaseTransaction() {

    const styles = {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        height: '5vh',
    };

    const styleTransaction = {
        margin: '10px',
        padding: '25px',
        textAlign: 'left'
    }

    const styleErrorMsg = {
        color: 'red'
    }

    const styleSuccessMsg = {
        color: 'green'
    }

    const [description, setDescription] = useState('')
    const [transactionDate, setTransactionDate] = useState('')
    const [purchaseAmount, setPurchaseAmount] = useState('')
    const [purchases, setPurchases] = useState('')
    const [currencies, setCurrencies] = useState('')
    const [selectedCurrency, setSelectedCurrency] = useState('')
    const [currentRates, setCurrentRates] = useState('')

    const [errorDescription, setErrorDescription] = useState('')
    const [errorDate, setErrorDate] = useState('')
    const [errorAmount, setErrorAmount] = useState('')
    const [serverError, setServerError] = useState('')
    const [serverSuccess, setServerSuccess] = useState('')

    const handleClickAdd = (e) => {
        e.preventDefault()
        var send = true;
        // Some form validations...   

        // Description
        if (description === '') {
            setErrorDescription("Empty description")
            send = false;
        } else {
            setErrorDescription("");
        }

        // Transaction date
        var regex = /^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
        if (transactionDate === '') {
            setErrorDate("Empty transaction date")
            send = false;
        } else
            if (!regex.test(transactionDate)) {
                setErrorDate("Invalid date format. Use YYYY-MM-DD")
                send = false;
            } else {
                setErrorDate("");
            }

        // Purchase amount
        var regex = /^[1-9]\d*(((,\d{3}){1})?(\.\d{0,2})?)$/;
        if (purchaseAmount === '') {
            setErrorAmount("Empty purchase amount")
            send = false;
        } else
            if (!regex.test(purchaseAmount)) {
                setErrorAmount("Invalid purchase amount");
                send = false;
            } else {
                setErrorAmount("");
            }

        if (!send) {
            return;
        }

        const purchaseTransaction = { description, transactionDate, purchaseAmount }
        fetch(
            "http://localhost:8080/purchase/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(purchaseTransaction)
        }).then((res) => {
            if (res.ok) {
                console.log("New purchase transaction added.")
                setServerSuccess("New purchase transaction added.")
                setServerError("")
                setDescription("")
                setTransactionDate("")
                setPurchaseAmount("")
            } else {
                res.text().then(text => {
                    console.error("Error adding new purchase transaction.")
                    setServerError("Error on server side. " + text)
                    setServerSuccess("")
                })
            }
        })
    }

    const onCurrencyChange = (event, value) => {
        setSelectedCurrency(value);
    }

    const handleClickShow = (e) => {
        e.preventDefault()
        fetch("http://localhost:8080/purchase/get-all-converted?currency=" + selectedCurrency)
            .then(response => response.json())
            .then(data => setPurchases(data))
    }

    useEffect(() => {
        fetch("http://localhost:8080/exchange-rate/get-available-currencies")
            .then(response => response.json())
            .then(data => setCurrencies(data))

        var tmpCurrentRates = 'Most recent rates (for 1.00 USD): ';

        fetch("http://localhost:8080/exchange-rate/get-current-exchange-rates")
            .then(response => response.json())
            .then(data => {
                data.map(rate => {
                    // Prints only ~20% of the currencies
                    if (Math.random() <= 0.2) {
                        var rateTwoDecimals = Number(rate.exchange_rate).toFixed(2);
                        tmpCurrentRates += rate.currency + ' (' + rate.country + ') - ' + rateTwoDecimals + ' | ';
                    }
                });
                setCurrentRates(tmpCurrentRates);
            })
    }, [])

    return (
        <Box
            component="form"
            sx={{
                '& > :not(style)': { m: 1 },
            }}
            noValidate
            autoComplete="off"
        >
            <Container>
                <h2>Add Purchase Transaction</h2>
                <form className='{classes.root}' noValidate autoComplete='off'>
                    <TextField error={errorDescription} helperText={errorDescription} label="Description" variant="filled" value={description} onChange={(e) => setDescription(e.target.value)} />&nbsp;
                    <TextField error={errorDate} helperText={errorDate} label="Date (YYYY-MM-DD)" variant="filled" value={transactionDate} onChange={(e) => setTransactionDate(e.target.value)} />&nbsp;
                    <TextField error={errorAmount} helperText={errorAmount} label="Amount in USD (#.##)" variant="filled" value={purchaseAmount} onChange={(e) => setPurchaseAmount(e.target.value)} />&nbsp;
                    <br /><br />
                    {(serverError != '') ?
                        <div style={styleErrorMsg}>{serverError}</div> : null
                    }
                    {(serverSuccess != '') ?
                        <div style={styleSuccessMsg}>{serverSuccess}</div> : null
                    }
                    <Button variant="contained" onClick={handleClickAdd}>Add Purchase</Button>
                </form>
            </Container>
            <div id="scroll-container">
                <div id="scroll-text">{currentRates}</div>
            </div>
            <h2>Transaction Purchases</h2>
            <br />
            <form className='{classes.root}' noValidate autoComplete='off'>
                <div style={styles}>
                    <Autocomplete
                        disablePortal
                        id="combo-box-demo"
                        options={currencies}
                        sx={{ width: 300 }}
                        onChange={onCurrencyChange}
                        renderInput={(params) => <TextField {...params} label="Currencies" />}
                    />
                    &nbsp;&nbsp;
                    <Button variant="contained" onClick={handleClickShow}>Show All Purchases</Button>
                </div>
            </form>
            <br />
            {Array.isArray(purchases)
                ? purchases.map(purchase => (
                    <Paper style={styleTransaction} key={purchase.transactionId}>
                        <b>Transaction Id {purchase.transactionId}: {purchase.description}</b><br />
                        Date: {purchase.transactionDate}<br />
                        Amount in USD: {Number(purchase.purchaseAmount).toFixed(2)}<br />
                        Exchange rate: {purchase.exchangeRate ? Number(purchase.exchangeRate).toFixed(2) : <span style={styleErrorMsg}>purchase cannot be converted to the target currency</span>}<br />
                        Converted purchase amount: {purchase.convertedPurchaseAmount ? Number(purchase.convertedPurchaseAmount).toFixed(2) : <span style={styleErrorMsg}>purchase cannot be converted to the target currency</span>}
                    </Paper>
                ))
                : null}
        </Box>
    );
}