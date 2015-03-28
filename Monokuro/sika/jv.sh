java -jar Monokuro.jar -d ~/Codes/xcode/SenhonSika/SenhonSika/XB353/ XB353
java -jar Monokuro.jar -d ~/Codes/xcode/SenhonSika/SenhonSika/XBFL/ XBFL
java -jar Monokuro.jar -d ~/Codes/xcode/SenhonSika/SenhonSika/XBII/ XBII
java -jar Monokuro.jar -d ~/Codes/xcode/SenhonSika/SenhonSika/DJGC/ DJGC

echo "all to 2-bit png..."

java -jar Monokuro.jar -d! XB353 XB353MNC
java -jar Monokuro.jar -d! XBFL XBFLMNC
java -jar Monokuro.jar -d! XBII XBIIMNC
java -jar Monokuro.jar -d! DJGC DJGCMNC

echo "done"
