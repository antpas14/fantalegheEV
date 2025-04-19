![Build passing](https://github.com/antpas14/fantalegheEV/actions/workflows/master.yml/badge.svg)
![Coverage Status](https://codecov.io/gh/antpas14/fantalegheEV/branch/master/graph/badge.svg?token=Vq0xlNvpFJ)

# Fantaleghe EV

This is a Java implementation of [fantalegheEV project](https://github.com/antpas14/fantalegheEV-api)

To summarise this project is a web application that permits to recalculate a fantasy league rank in a *fair* way, potentially addressing discrepancies or providing alternative ranking methods.

This application backend runs on <a href="spring.io">Spring Framework</a> and uses Java library <a href="https://poi.apache.org/">Apache POI</a> to parse a Microsoft Excel Spreadsheet (xlsx) that can be downloaded from the league homepage (only logged users are permitted to do so).

A `docker-compose.yml` is provided for easy setup. You can start the application and its dependencies using `docker-compose up`. This setup also includes a basic UI that can be found <a href="https://github.com/antpas14/fantalegheEV-ui">here</a>.

**Note:** this UI relies on an older API and is not usable at the moment. There are plans to update it to work with the current API.

This application analyzes football fantasy league hosted on <a href="http://leghe.fantacalcio.it">leghe.fantacalcio.it</a>. I have no relationship with them.

### License

This work is distributed under MIT license.
