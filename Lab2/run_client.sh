#!/bin/bash
for (( i = 0; i < 7; i++ )); do
  if [[ ${i} == 6 ]]; then
    wantResults=true
  else
    wantResults=false
  fi
  if [[ $(( ${i} % 2 )) == 0 ]]; then
    candidate="Candidate_1"
  else
    candidate="Candidate_2"
  fi
  java ElectionClient localhost 1099 ${i} ${candidate} ${wantResults}
done
