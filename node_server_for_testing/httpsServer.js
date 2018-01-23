var https = require('https');
var fs = require('fs');

var options ={
  key:fs.readFileSync('privateKey.key'),
  cert: fs.readFileSync('certifcate.crt')
};

function getData(){
  https.createServer(options,function(req,res){
    res.writeHead(200);
    res.end("apna https");
  }).listen(8087);
}

function printPostData(){
  https.createServer(options,function(req,res){
    var body ="";
    if(req.method=='POST'){
      req.on('data',function(data){
        body += data;

      });
      req.on('end',function(){
        console.log(body);
        res.writeHead(200, "OK", {'Content-Type': 'text/plain'});
            res.end("post successfull!!!");
      });
    }
    if(req.method == 'GET'){
      res.writeHead(200,"OK",{'Content-Type': 'applicaion/json'});
      var item ={
        "id":2,
        "givenName":"retrievedGivenName",
        "lastName":"retrievedLastName",
        "email":"retievedEmail"
      };
      var myJSON = JSON.stringify(item);
      res.end(myJSON);
    }
  }).listen(8087);
}

//getData();
printPostData();
console.log("https listening at 8087");
