import 'package:http/http.dart' as http;
import 'dart:convert' as convert;

import 'package:shop_app_front/api/model/category.dart';
import 'package:shop_app_front/api/model/product.dart';

 class CatService {

  Future<List<Category>> getCategories() async{

        final baseUrl = Uri.parse("http://localost:3000");

        final response = await http.get(baseUrl);

        if (response.statusCode < 200 || response.statusCode > 300){
            throw Exception("response : error");
        }else{

        var jsonResponse = convert.jsonDecode(response.body) as List<Map<String, dynamic>>;

        List<Category> categories;

        for(category in jsonResponse){


        }

        }
    }

    Category mapToCategory(Map<String, dynamic> jsonCategory){
    
        final category = Category(id: jsonCategory["id"], name: jsonCategory["name"]);

    }

    List<Product> mapToProductList(List<Map<String, dynamic>> jsonProducts){
        
        List<Product> products;

        for (var prod  in jsonProducts) {
                  
            products.add(Product(name: prod["name"], description: prod["description"], price: prod["price"], imageUrl: prod["imagePath"]  ));
        }

    }

 }
