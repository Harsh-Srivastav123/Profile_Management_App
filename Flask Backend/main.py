import json

import pymongo
from flask import Flask, render_template, request, redirect,jsonify;
from pymongo import MongoClient
from bson.objectid import ObjectId

app = Flask(__name__)
# app.config["MONGO_URI"] = "mongodb+srv://windowsharsh2003:crud_flask_h%40rsh123@cluster0.dhuyhpo.mongodb.net/test"
# db = MongoClient('localhost', 27017)
CONNECTION_STRING="mongodb+srv://windowsharsh2003:crud_flask_h%40rsh123@cluster0.dhuyhpo.mongodb.net/test"

try:
    db = pymongo.MongoClient(CONNECTION_STRING)
    database = db.get_database("Collection_name")
except:
    print("error occured")
@app.route('/main', methods=['GET', 'POST'])
def home():
    collection = database["Collection_name"]
    # collection.insert_one({"Name": "Harsh raj Srivastav",
    #                        "class": "Btech"})
    # collection.insert_one({"Name": "New One",
    #                        "class": "Btech"})
    items = []
    for doc in collection.find():
        items.append(doc)
    print(items)
    return json.dumps(items,default=str)

    # items=collection.find()
    # for rows in items:
    #     data_row.append(rows)
    #
    # return json.dumps(data_row,default=str)


# # enter new user
# @app.route('/newuser/<str:word>/<str:standard>',methods=['GET','POST'])
# def newUser(word,standard):
#     collection = database["Collection_name"]
#     collection.insert_one({"Name": word,
#                             "classes": standard})

#


# display students content
@app.route('/display', methods=['GET', 'POST'])
def display():
    collection = database["Students"]
    items = []
    for doc in collection.find():
        items.append(doc)
    print(items)
    return json.dumps(items,default=str)


@app.route('/add',methods=['POST'])
def addUser():

    jsonobj=request.json
    print(str(jsonobj))
    name=jsonobj['Name']
    email=jsonobj['Email']
    About=jsonobj['About']
    url=jsonobj['Img_Url']
    date_time=jsonobj['date_time']
    student=database["Students"]
    student.insert_one({"Name":name,
                        "Email":email,
                        "date_time":date_time,
                        "About":About,
                        "Img_Url":url})
    #must be optimized
    x=str(student.find_one({'Name':name},{'Name':1}))
    print(x)
    y=x.find(name)
    print(y)
    if(y>0):
        return json.dumps({"message_code":200},default=str)
    else:
        return json.dumps({"message_code":100},default=str)


@app.route('/update',methods=['POST'])
def updateUser():
    jsonobj=request.json
    _id=jsonobj['_id']
    name = jsonobj['Name']
    email = jsonobj['Email']
    About = jsonobj['About']
    url = jsonobj['Img_Url']
    date_time = jsonobj['date_time']
    try:
        x = "_id"
        student = database["Students"]
        student.update_one(
            {x: ObjectId(_id)},
            {"$set": {"Name": name,"Email":email,"About":About,"url":url,"date_time":date_time}}
        )
        return json.dumps({"message_code": 201}, default=str)
    except:
        return json.dumps({"message_code": 101}, default=str)


@app.route('/delete',methods=['POST'])
def delete():
    jsonobj=request.json
    _id=jsonobj['_id']
    Id=str(_id)
    try:
        student = database["Students"]
        student.delete_one(
            {'_id':ObjectId(Id)}
        )
        return json.dumps({"message_code": 301}, default=str)
    except:
        return json.dumps({"message_code": 300}, default=str)



if __name__ == '__main__':
    app.run(debug=True,port='8080')
