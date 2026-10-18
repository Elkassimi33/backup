import 'package:http/http.dart' as http;
import 'dart:convert' as convert;
import 'dart:collection';
import 'package:shop_app_front/api/model/request/CategoryRequest.dart';
import 'package:shop_app_front/api/model/product.dart';

class CategoryService {
  static final String baseUrl = "http://localhost:8080/api/";

  static CategoryService? _categoryService;
  static HashSet<Product> _favoriteProducts = HashSet<Product>();

    CategoryService._internal();

  static CategoryService getInstance() {
    _categoryService ??= CategoryService._internal();
    return _categoryService!;
  }

    static HashSet<Product> getFavoriteProducts() {
    return _favoriteProducts;
  }

  static void addFavorite(Product product) {
    _favoriteProducts.add(product);
  }

  static void removeFavorite(Product product) {
    _favoriteProducts.remove(product);
  }

  static bool isFavorite(Product product) {
    return _favoriteProducts.contains(product);
  }

  Future<List<Product>> getCategoryProducts(String categoryName) async {
    final response = await http.get(
      Uri.parse(baseUrl + "products/category/$categoryName"),
    );

    if (response.statusCode != 200) {
      throw Exception("Something went wrong please try again!");
    }

    var jsonResponse =
        convert.jsonDecode(response.body) as List<dynamic>;

    List<Product> productList = [];

    for (Map<String, dynamic> prod in jsonResponse) {
      productList.add(_mapToProduct(prod));
    }

    return productList;
  }

  Future<List<CategoryRequest>> getAllCategories() async {
    final response = await http.get(Uri.parse(baseUrl + "categories"));

    if (response.statusCode != 200) {
      throw Exception("Something went wrong please try again!");
    }

    final List<dynamic> jsonResponse = convert.jsonDecode(response.body);
    List<CategoryRequest> categoryList = [];

    for (Map<String, dynamic> cat in jsonResponse) {
      categoryList.add(_mapToCategoryRequest(cat));

    }

    return categoryList;
  }

    Future<int> getProductCount(String categoryName)async{
       final response = await http.get(Uri.parse(baseUrl+"categories/$categoryName/product-count"));
        if (response.statusCode != 200) {
            throw Exception("Something went wrong please try again!");
        }

         var res = convert.jsonDecode(response.body);
        return res;
    }


  CategoryRequest _mapToCategoryRequest(Map<String, dynamic> json) {
    return CategoryRequest(
      categoryId: json["categoryId"],
      name: json["name"],
      imageUrl: json['imageUrl'],
      description: json["description"],
    );
  }

  Product _mapToProduct(Map<String, dynamic> json) {
    return Product(
      productId: json["productId"],
      name: json["name"],
      categoryName: json['categoryName'],
      description: json["description"],
      price: json["price"],
      discount: json["discount"],
      imageUrl: json["imageUrl"],
    );
  }
}
