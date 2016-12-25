#Medical-Conference-Reminder

####An Android application that provides managing and tracking of medical conference of doctors.
Time of work - 17h

##Functional Specification

<p>An Android application that doctors can use to participate in and track medical conference schedules. Database is used as an emulator of server - we assume that multiple users works on single device (that is task spec), wich means our local database, is, actually, server database </p>

####For conference admins
 - can schedule new conferences
 - can send conference calendar invites to doctors
 - can see suggested new/old topics
 
####For doctors
 - can suggest new topics they could speak on
 - can see suggested new/old topics
 - can receive / accept and reject invites
 - invites are added to calendar

####You can see demo video in <a href = "https://github.com/spartacus777/ConferenceReminder/tree/master/Demo">Demo folder</a>. 

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
 <p align="center">
  <img src="https://s29.postimg.org/4xb1gns5j/Screenshot_2016_11_23_18_31_51.png" width="250"/>
  <img src="https://s24.postimg.org/3ld51ugg5/Screenshot_2016_11_23_18_32_11.png" width="250"/>
  <img src="https://s28.postimg.org/kyoe6ybv1/Screenshot_2016_11_23_18_32_37.png" width="250"/>
</p>
<p align="center">Registration/Login screens</p></br>

 <p align="center">
  <img src="https://s23.postimg.org/gjkjb3xez/Screenshot_2016_11_23_18_33_01.png" width="250"/>
  <img src="https://s27.postimg.org/qvmj3eeer/Screenshot_2016_11_23_18_33_03.png" width="250"/>
</p>
<p align="center"> Main Ativity with two fragments - profile and conference list</p></br>

 <p align="center">
 <img src="https://s23.postimg.org/bv6vs77nv/Screenshot_2016_11_23_18_33_27.png" width="250"/>
  <img src="https://s24.postimg.org/dpcvuoa6t/Screenshot_2016_11_23_18_33_31.png" width="250"/>
  <img src="https://s28.postimg.org/bjcug6xfx/Screenshot_2016_11_23_18_33_35.png" width="250"/> 
</p>
<p align="center">Here we create new conference, and invite doctors to it</p></br>


 <p align="center">
 <img src="https://s27.postimg.org/3znnshcmr/Screenshot_2016_11_23_18_38_09.png" width="250"/>
  <img src="https://s29.postimg.org/5dot61fmf/Screenshot_2016_11_23_18_38_34.png" width="250"/>
</p>
<p align="center"> We log out, and log in as a doctor. we can see third fragment, called Invite, we can accept/reject it. also, we can suggest topics to the conference</p></br>


####How to build: 
To build proj, please use Android studio with gradle. You will need API 25 SDK installed on your machine. 

####Initialize DB - not required 
DB will initialise itself with demo data. 

