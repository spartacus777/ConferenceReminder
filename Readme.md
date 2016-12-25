#Medical-Conference-Reminder

####An Android application that provides managing and tracking of medical conference of doctors.
Time of work - 17h

##Functional Specification

####An Android application that doctors can use to participate in and track medical conference schedules.

####For conference admins
 - can schedule new conferences
 - can send conference calendar invites to doctors
 - can see suggested new/old topics
####For doctors
 - can suggest new topics they could speak on
 - can see suggested new/old topics
 - can receive / accept and reject invites
 - invites are added to calendar

####You can see demo video in Demo folder

##Features, implemented:
 - Choose image profile picture form camera or gallery
 - Crop photo functionality for profile picture – developed nice widget(view actually) which can scale and crop image, so we would have nice photo in profile.
 - Popular ORM - GreenDao library for database 
Before I used Active Android ORM, but it is deprecated now
I like ORm decisions much more than writing SQLite code myself, since it is much faster, and safer, less bugs are produced. And, as I know, Green Dao ORM is among fastest orms available for android. 
 - 	UniversalImageLoader for photo loading and caching 
I always use this awesome lib, since it can cache in memory and disc, and load photos as from internet and as from disc. So, add server support to app in future would be easier, since I load all (local) photos using UIL.
 - ButterKnife bindings Easy binding views from xml to java, less boilerplate code is produced.
 - Recycler View used for all lists, presented in app – it is much better than ListView.
 
 ##Some screenshots
 
 (https://s29.postimg.org/4xb1gns5j/Screenshot_2016_11_23_18_31_51.png)

1. How to build: 
To build proj, please use Android studio with gradle. You will need API 25 SDK installed on your machine. 

2. Initialize DB - not required 
DB will initialise itself with demo data. 

