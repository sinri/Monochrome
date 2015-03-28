cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/
echo "begin"

cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/XB353MNC
for i in *.mnc
do
    if test -f "$i" 
    then
       echo "zip $i"
       zip ../zipped/XB353MNC/"$i".zip $i
    fi
done

cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/XBFLMNC
for i in *.mnc
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       zip ../zipped/XBFLMNC/"$i".zip $i        
    fi
done

cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/XBIIMNC
for i in *.mnc
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       zip ../zipped/XBIIMNC/"$i".zip $i        
    fi
done

cd /Users/Sinri/Codes/Monochrome/Monokuro/dist/DJGCMNC
for i in *.mnc
do
    if test -f "$i"
    then
       echo "Doing somthing to $i"
       zip ../zipped/DJGCMNC/"$i".zip $i        
    fi
done
