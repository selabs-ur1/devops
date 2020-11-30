const axios = require('axios')
const express = require('express')
const app = express()
const apiKey = 'b758b8cf956740b8bff95022e6d6bc8a'
const url = 'https://api.weatherbit.io/v2.0/forecast/daily'

app.get('/api/weather/:zipcode', (req, res) => {
	axios.get(url, {
		params: {
			key: apiKey,
			postal_code: req.params.zipcode,
			country: 'FR'
		}
	}).then(response => {
		console.log(response.data)
		res.json(response.data)
	}).catch(err => {
		res.json(err)
	})
})

app.listen(8081)
