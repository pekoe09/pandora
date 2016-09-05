# Pandora

[![Build Status](https://travis-ci.org/pekoe09/pandora.svg?branch=master)](https://travis-ci.org/pekoe09/pandora)

System for managing collections of any collectible items. Provides functionality to set up multiple _collections_, 
with a hierarchy of _sets_ of collectibles. Each set can contain _slots_ for items to collect and each slot can in turn
contain multiple _items_, i.e. actual things in user's collection. In addition, user can record _sightings_ of items on sale
in _sales venues_. User can upload his/her own images, attaching them on items, sightings or slots.

UI is currently in Finnish only; will be upgraded to support both Finnish and English in the future.

Pandora is a Java Spring MVC application, with JPA/Hibernate backend. Pandora is hosted on Heroku and uploaded images are 
stored in an AWS S3 bucket.
