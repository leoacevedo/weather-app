# weather-app
Simple native Android weather app written in Kotlin for a code challenge.


## How to use it

To search for a specific city, just tap on the search icon in the app bar for the search bar to open. Then either type the city's name, or tap once again to see your recent searches, if any.

On the recent searches list, tap on the cross button to remove that entry.

**Extra functionality**: If you want to switch between metric and imperial units, do so from the three-dotted menu (ie. the overflow menu). 


## Architecture 

The source is separated in modules following [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)'s canonical four layers:

* **model**: just data clases and a constant to represent the number of search history items the app should keep on local storage
* **use cases**: an interface for the only actual use case in this app: retrieve a weather forecast for a specific city
* **interface adapters**: classes for MVP and domain-specific repositories, with as little reference to specific frameworks as possible. Repositories have actual code, whereas classes whose implementation depends on a framework (ie. Android, Retrofit, Room) are modelled with interfaces only.
* **frameworks and drivers**: this is the `app` module, which has all the implementations required in the previous layer. 

I used MVP to structure the communication between the model and the views. I like better MVVM and MVI, but really they require more code to be written, and since this is a challenge project, I don't think I can use a library such as [mosby](https://github.com/sockeqwe/mosby), which would have been ideal.
Also, because this is a challenge project, I did not write a generic Presenter class from which to inherit my weather presenter (ie. I did no framework-like code).

The threading relies completely on Kotlin coroutines. Honestly I am no expert on this library, which is precisely why I chose it for this challenge, as opposed to RxJava.

## Last but not least

Every (constructive) comment is welcome!
