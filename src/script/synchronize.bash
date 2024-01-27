#!/bin/bash

LASTYEAR=2024
for year in $(seq 2020 $LASTYEAR)
do
  # echo year: $year
  for month in 1 3 6 9 # $(seq 1 12)
  do
    #echo month: $month
    for day in 1 3 9 #$(seq 1 31)
    do
      #echo day: $day
      date=$(printf "%04d-%02d-%02d" $year $month $day)
      echo curl http://localhost:8080/openbarelichamen?validAt=$date
      curl http://localhost:8080/openbarelichamen?validAt=$date
      curl http://localhost:8080/bestuurlijkegebieden?validAt=$date
    done
  done
done