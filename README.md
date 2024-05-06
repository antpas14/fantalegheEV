![Build passing](https://github.com/antpas14/fantalegheEV/actions/workflows/master.yml/badge.svg)
![Coverage Status](https://codecov.io/gh/antpas14/fantalegheEV/branch/master/graph/badge.svg?token=Vq0xlNvpFJ)

# Fantaleghe EV

This is a Java implementation of [fantalegheEV project](https://github.com/antpas14/fantalegheEV-api)

To summarise this project is a web application that permits to recalculate a fantasy league rank in a *fair* way.
This application backend runs on <a href="spring.io">Spring Framework</a> and uses Java library <a href="https://jsoup.org">jsoup</a> to perform HTML parsing
The app needs to connect to an app like <a href="https://github.com/antpas14/webFetcher">webFetcher</a>, a simple app that returns the HTML of the requested page after some javascript rendering is executed.

A docker compose is provided which also utilises a basic UI that can be found <a href="https://github.com/antpas14/fantalegheEV-ui">here</a>

This application analyzes football fantasy league hosted on <a href="http://leghe.fantacalcio.it">leghe.fantacalcio.it</a>. I have no relationship with them.

### License

This work is distributed under MIT license.
