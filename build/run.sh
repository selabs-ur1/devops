
export exitcode=0
mkdir /var/workspace/target
touch /var/workspace/target/coverage.svg

# install dependencies
npm install

# static angular documentation
npm run compodoc
export var=$?
if [[ "$var" -ne "0" ]]; then
  echo "fail"
  export exitcode=1
else
  echo "success"
fi

# coverage percentage
export cov=`grep -E "Statements.*?([0-9]+(?:\.[0-9]+)?)%" test.log | cut -d "%" -f1 | awk  '{ printf( "%s ", $3 ); } END { printf( "\n" ); }'`

rm test.log
exec "$@"

exit $exitcode

