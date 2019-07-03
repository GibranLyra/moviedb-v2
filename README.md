# Rappi Test
### A demonstration of MVVM pattern using the trending libraries of the android world and Android Architecture Components.
 
App is based on the [Google TODO MVVM pattern](https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava), this pattern was choosed to make the business logic easily testable.

The styleguide used is the [Google CodeStyle](https://github.com/google/styleguide)

##Some Notes

- I created a exclusive module exclusively to demonstrate a way to let the service be complete decoupled from the app. That helps when the service is the same in multiple front ends.
- Unit tests aren`t made in this project due to difficulties utilizing AndroidX components, if a sample of unit tests are needed i have a sample in [another project](https://github.com/GibranLyra/amaroTest). That repository have Unit and instrumentation tests using JUnit and Espresso.

 # Tests locations
 - Unit tests under [test folder](https://github.com/GibranLyra/amaroTest/tree/master/app/src/test/java/com/example/gibranlyra/amarotest).
 - Instrumentation test under [androidtests folder](https://github.com/GibranLyra/amaroTest/blob/master/app/src/androidTest/java/com/example/gibranlyra/amarotest/HomeScreenTest.java).

 - 
## Used Libraries
- [Retrofit](http://square.github.io/retrofit)
- [RxJava2](https://github.com/ReactiveX/RxJava)
- [Timber](https://github.com/JakeWharton/timber)
- [Glide](https://github.com/bumptech/glide)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
 
## Test Questions

##### Las capas de la aplicación (por ejemplo capa de persistencia, vistas, red, negocio, etc) y qué clases pertenecen a 
- R: Persistence are made using room and the parttern utilizing the [RepositoryPattern](https://deviq.com/repository-pattern/) under the MovieDbService module

All bussiness logic are under the viewModel classes

##### La responsabilidad de cada clase creada. 
- R: Repository class decides if the data comes from remote or local datasource. ViewModels are responsibles to bussiness logic and View classes are responsible to show the data on the screen.

##### En qué consiste el principio de responsabilidad única? Cuál es su propósito? 
- R: Basically it consists in giving classes a single responsability, that make easier to change the dependencies, reduce coupling and encapsulate the roles of each class. In this project we have three separated major roles: Data, Bussiness logic and views.

##### Qué características tiene, según su opinión, un “buen” código o código limpio? 
- R: Low coupling, high coesion, a strong guideline and code as documentation.

## What I would change
- I would add genres and some more information to details screen, figure out why I was having problemas with room and Rxjava in mockito(Unit Tests) and add some Instrumentation tests.
- I would use android navigation components that is still in alpha 

License
==========

Copyright (c) GibranLyra 2018

GNU GENERAL PUBLIC LICENSE

Version 3, 29 June 2007

See LICENSE.md
