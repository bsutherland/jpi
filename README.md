# jpi

API for looking up Japanese addresses by postal code. Useful for filling out forms from entered postal code - a fairly standard feature on Japanese websites. Built to learn Scala/Play.

## Data Store

Redis, via the [plugin for the Play framework](https://github.com/typesafehub/play-plugins/tree/master/redis)

## Source Data

Zipped CSV file from the [Japan Post website](http://www.post.japanpost.jp/zipcode/download.html) (Japanese only)

## API

### GET /*{postal_code}*

**Content Type** application/json; charset=utf-8

**Character Encoding** UTF-8

**Response** 200 OK

**Errors** 404 Not Found - Invalid postal code, or postal code does not exist.

**Typical Response**

```
{
  "prefecture_kana":"ﾄｳｷｮｳﾄ",
  "municipality_kana":"ｼﾅｶﾞﾜｸ",
  "neighbourhood_kana":"ﾐﾅﾐｵｵｲ",
  "prefecture_kanji":"東京都",
  "municipality_kanji":"品川区",
  "neighbourhood_kanji":"南大井"
}
```

### POST /rebuild

Download zipped CSV list of postal codes and update the data store. In production this URL should be accessible by administrators only.

**Response** 200 OK

**Errors** 500 Internal Server Error - Something went wrong while attempting to download, extract, parse or insert the data.
