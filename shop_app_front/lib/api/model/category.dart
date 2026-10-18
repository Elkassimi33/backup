 import 'package:shop_app_front/api/model/product.dart';

class Category {

     final int id;
     final String name;
     final List<Product> products;

     const Category({required this.id,required this.name, required this.products});


 }
