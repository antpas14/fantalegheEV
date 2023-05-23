![Build passing](https://github.com/antpas14/fantalegheEV/actions/workflows/master.yml/badge.svg)
![Coverage Status](https://codecov.io/gh/antpas14/fantalegheEV/branch/master/graph/badge.svg?token=Vq0xlNvpFJ)

# Fantaleghe EV

This project is a web application that permits to recalculate a fantasy league rank in a *fair* way.
This application backend runs on <a href="spring.io">Spring Framework</a> and uses Java library <a href="https://jsoup.org">jsoup</a> to perform HTML parsing
The app needs to connect to an app like <a href="https://github.com/antpas14/webFetcher">webFetcher</a>, a simple app that returns the HTML of the requested page after some javascript rendering is executed.
Frontend for the application is realized using <a href="https://angularjs.org/">AngularJs</a> and can be found <a href="https://github.com/antpas14/fantalegheEV-ui">here</a>

In the provided docker-compose.yml there is an example setup that can be used to run it locally. 

This application analyzes football fantasy league hosted on <a href="http://leghe.fantacalcio.it">leghe.fantacalcio.it</a>. I have no relationship with them.  

### Fantasy football rules
This game is based on Italian Serie A football championship.

A league is formed by teams which roaster is composed by Serie A clubs' players.
A league day is associated to a Serie A championship day. For each league day a team picks up eleven player for the starter team and a variable number of players to compose the bench.
After the game championship is ended is possible to calculate league results.
A team scores N goals based on grades (1 to 10) given to players by different Italian sports newspapers (source is configurable). Also malus/bonus are applied to a player's grade if a certain event happens (a red card is usually a -1, a goal scored is usually a +3. This values are configurable by a league admin and can change).

In a *Championship* type league a team plays with all others team in the league: since team goals scored are independent from adversary team goals scored in a head to head match, luck in the drawing phase may have have a great impact on the league ranking.

This is way I come up with an algorithm which can give an estimate on how lucky, or unlucky, teams in the league are.
### Algorithm

Algorithm calculate all possible outcomes for each league day then it returns an average value

An example would be:
   
    TeamA 3 - TeamB 3
    TeamC 1 - TeamD 0
   
After this league game, ranking is:

    TeamC      3 
    TeamA      1    
    TeamB      1 
    TeamD      0 

Using the algorithm, we will have a new ranking:
    
    TeamA and TeamB: 1 + 3 + 3 = 7/3 = 2.33
    TeamC: 3 + 0 + 0 = 3/3 = 1
    TeamD: 0 + 0 + 0 = 0/3 = 0
    
### Sites screenshots
UI is quite basic and just requires the league name

![Homepage](docs/images/fantaleghe_empty.png)

Then click on the calculate button

![After calculation](docs/images/fantaleghe_filled.png)

After calculation is made, rank will appear on bottom. It contains:
* points calculated by the algorithm 
* actual point earned by the team
* difference between them

A *positive* difference gives an estimate on how the team has been *lucky* until now. Otherwise, a *negative* difference gives an estimate on how the team has been *unlucky*.

### License

This work is distributed under MIT license.
