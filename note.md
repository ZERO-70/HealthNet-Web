# note

the user can register with some personId that has already been assigned to some other user
need to work on that to restrcit them in choosing different or null as personId , i am adding null
because , when user gets register , the person details will be added after wards . so during registration
the person details are unknown.

I also need to make sure if someone is acessing its own data i.e /doctors/{id} , it is acessing its own data
and not someone elses , i will probably make this check in service layer as did earlier once.