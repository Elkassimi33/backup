import 'package:flutter/material.dart';
import 'package:shop_app_front/api/model/request/CategoryRequest.dart';
import 'package:shop_app_front/widgets/CategoryWidget.dart';
import 'package:shop_app_front/api/service/catService.dart';
import 'package:shop_app_front/widgets/MyAppBar.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final CategoryService categoryService = CategoryService.getInstance();
  List<CategoryRequest> categories = await categoryService.getAllCategories();

  runApp(MyApp(categories: categories, categoryService: categoryService));
}

class MyApp extends StatelessWidget {
  final List<CategoryRequest> categories;
  final CategoryService categoryService;

  const MyApp({super.key, required this.categories,required this.categoryService});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: HomePage(categories: categories, categoryService: categoryService,),
      debugShowCheckedModeBanner: false,
    );
  }
}


class HomePage extends StatelessWidget {
  final List<CategoryRequest> categories;

  final CategoryService categoryService;
  const HomePage({super.key, required this.categories,required this.categoryService});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: MyAppBar(title: "Categories"),
      body: MyPageBody(categories: categories, categoryService:  categoryService,),
    );
  }
}

class MyPageBody extends StatelessWidget {
  final List<CategoryRequest> categories;
  final CategoryService categoryService;
  const MyPageBody({super.key, required this.categories,required this.categoryService});

  @override
  Widget build(BuildContext context) {
    return GridView.count(
      primary: false,
      padding: EdgeInsets.all(20),
      crossAxisSpacing: 10,
      mainAxisSpacing: 10,
      crossAxisCount: 2,
      children: categories.map((category) {
        return CategoryWidget(
          categoryName: category.name,
          imageUrl: category.imageUrl,
          description: category.description,
                    categoryService: categoryService
        );
      }).toList(),
    );
  }
}
