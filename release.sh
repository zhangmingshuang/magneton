#!/bin/bash
#mvnd smart-doc:markdown -Dfile.encoding=UTF-8  -pl ./magneton-supports/magneton-support-api-auth -am

mvnd install -DskipTests -Dchangelist=.RELEASE

