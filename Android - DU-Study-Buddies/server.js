Parse.Cloud.beforeSave("StudyGroup", function(request, response) {
 
    var auth = request.object.get("Authorization");
    continueRequestIfLoggedIn(auth,
         function() {
                var user = auth.split(":")[0];
               request.object.unset("Authorization","");               
               request.object.set("Class",request.object.get("Class").toLowerCase().replace(/\s+/g, ''))
               if(request.object.isNew())
               {
                    request.object.set("Archived",false);
                    request.object.set("UsersAttending",[user])
                    request.object.set("Creator",user);
               }
               else
               {
                    if(request.object.get("Creator") != user)
                    {
                         
                    }
               }
               response.success();
           },
         response
      );
   
});
 
 
 
Parse.Cloud.define("MyStudyGroups", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              var user = request.params.Authorization.split(":")[0];
               
              query.ascending("StartTime");
              var d = new Date();
              var todaysDate = new Date(d.getTime());
              query.greaterThanOrEqualTo("EndTime",todaysDate);
 
              query.equalTo("Creator",user);
              query.equalTo("Archived",false);
              query.find({
                success: function(results) {
                    response.success(results);
 
                },
                error: function() {
                  response.error("No Groups Found");
 
                }
              });
 
        },
        response
    );
});
 
 
 
Parse.Cloud.define("AttendingStudyGroups", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              var user = request.params.Authorization.split(":")[0];
               
              query.ascending("StartTime");
              var d = new Date();
              var todaysDate = new Date(d.getTime());
              query.greaterThanOrEqualTo("EndTime",todaysDate);
              query.equalTo("Archived",false);
              query.equalTo("UsersAttending", user);
              query.find({
                success: function(results) {
                    response.success(results);
 
                },
                error: function() {
                  response.error("No Groups Found");
 
                }
              });
 
        },
        response
    );
});
 
 
 
Parse.Cloud.define("JoinStudyGroup", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              var user = request.params.Authorization.split(":")[0];
               
              query.get(request.params.GroupId, {
                  success: function(group) {
                    var attending = group.get("UsersAttending");
                    attending.push(user);
                    group.set("UsersAttending",attending);
                    group.set("Authorization",request.params.Authorization);
                    group.save(null, {
                          success: function(group) {
                             response.success(group);
                          },
                          error: function(gameScore, error) {
                             response.error(error);
                          }
                        });
                  },
                  error: function(object, error) {
                    response.error("Failed");
                  }
                });
 
        },
        response
    );
});
 
 
Parse.Cloud.define("LeaveStudyGroup", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              var user = request.params.Authorization.split(":")[0];
               
              query.get(request.params.GroupId, {
                  success: function(group) {
                    var attending = group.get("UsersAttending");
                    var index = attending.indexOf(user);
 
                    if (index !== -1) {
                        attending.splice(index, 1)
                        group.set("UsersAttending",attending);
                    }
                    group.set("Authorization",request.params.Authorization);
                    group.save(null, {
                          success: function(group) {
                             response.success(group);
                          },
                          error: function(gameScore, error) {
                             response.error(error);
                          }
                        });
                     
                  },
                  error: function(object, error) {
                    response.error("Failed");
                  }
                });
 
        },
        response
    );
});
 

  Parse.Cloud.define("DeleteStudyGroup", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              var user = request.params.Authorization.split(":")[0];
               
              query.get(request.params.GroupId, {
                  success: function(group) {
                      if(group.get("Creator") == user)
                      {
                        group.set("Archived",true);
                        group.set("Authorization",request.params.Authorization);
                        group.save(null, {
                            success: function(group) {
                                response.success(group);
                          },
                          error: function(gameScore, error) {
                              response.error(error);
                          }
                        });
                      }
                      else
                      {
                        response.error();
                      }
                    
                  },
                  error: function(object, error) {
                    response.error("Failed");
                  }
                });
 
        },
        response
    );
});



 
Parse.Cloud.define("QueryGroupsByClass", function(request, response) {
   
  Parse.Cloud.useMasterKey();
 
  continueRequestIfLoggedIn(
        request.params.Authorization,
        function(){
              var query = new Parse.Query("StudyGroup");
              if(request.params.classes)
              {
                 query.containedIn("Class",request.params.classes.toLowerCase().split(","))
              }           
              query.ascending("StartTime");
              var d = new Date();
              var todaysDate = new Date(d.getTime());
              query.greaterThanOrEqualTo("EndTime",todaysDate);
              query.equalTo("Archived",false);
              query.find({
                success: function(results) {
                    response.success(results);
 
                },
                error: function() {
                  response.error("No Groups Found");
 
                }
              });
 
        },
        response
    );
});
 
 
 
 
function continueRequestIfLoggedIn(auth , successFunction, response)
{

  Parse.Cloud.httpRequest({
      url: 'https://d1m.drexel.edu/API/v2.0/User/Roles',
      headers: {
        'Authorization': auth
      },
      success: function(httpResponse) {
        successFunction();
      },
      error: function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
        response.error("Failed Authorization")
      }
    });
 
 
}






