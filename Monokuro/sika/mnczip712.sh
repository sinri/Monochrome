echo "converting to 50% ...";
for i in ./XB712/*.jpg 
do
    if test -f "$i" 
    then
       echo "Doing somthing to $i"
       convert $i -resize 50% $i 
    fi
done

java -jar ../dist/Monokuro.jar -d XB712 XB712

echo "all to 2-bit png..."

java -jar ../dist/Monokuro.jar -d! XB712 XB712MNC

echo "done"

#cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/
echo "begin"

cd XB712MNC
for i in *.mnc
do
    if test -f "$i" 
    then
       echo "zip $i"
       zip ../zipped/XB712MNC/"$i".zip $i
    fi
done