import 'package:flutter/material.dart';
import 'package:shop_app_front/api/model/product.dart';
import 'package:shop_app_front/api/service/catService.dart';
import 'package:shop_app_front/widgets/Card.dart';

class FavoriteProductsWidget extends StatefulWidget {
  const FavoriteProductsWidget({super.key});

  @override
  State<FavoriteProductsWidget> createState() => _FavoriteProductsWidgetState();
}

class _FavoriteProductsWidgetState extends State<FavoriteProductsWidget> {
  List<Product> _products = [];

  @override
  void initState() {
    super.initState();
    _products = CategoryService.getFavoriteProducts().toList();
  }

  void _updateState() {
    setState(() {
      _products = CategoryService.getFavoriteProducts().toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    if (_products.isEmpty) {
      return const Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.favorite_border, size: 64, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              'No favorites yet',
              style: TextStyle(fontSize: 18, color: Colors.grey),
            ),
          ],
        ),
      );
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
          child: Text(
            'Favorites',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        Expanded(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 16,
                mainAxisSpacing: 16,
                childAspectRatio: 0.7,
              ),
              itemCount: _products.length,
              itemBuilder: (context, index) {
                final product = _products[index];
                return MyCard(
                  product: product,
                  updateTheState: _updateState,
                );
              },
            ),
          ),
        ),
      ],
    );
  }
}
