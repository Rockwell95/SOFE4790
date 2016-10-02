#!/usr/bin/env bash
for (( i = 4; i < 8; i++ )); do
  if [[ ${i} == 7 ]]; then
    wantResults=true
  else
    wantResults=false
  fi
  if [[ $(( ${i} % 3 )) == 0 ]]; then
    candidate="Candidate_1"
  else
    candidate="Candidate_2"
  fi
  java ElectionClient localhost 1099 ${i} ${candidate} ${wantResults}
done
