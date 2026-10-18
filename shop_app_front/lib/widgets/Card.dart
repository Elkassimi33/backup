import 'dart:math';

import 'package:flutter/material.dart';
import 'package:shop_app_front/api/model/product.dart';
import 'package:shop_app_front/api/service/catService.dart';

class MyCard extends StatefulWidget {

    final Product product;
    final int rating;
    final VoidCallback? updateTheState;


    MyCard({super.key, required this.product,this.updateTheState }): rating = Random().nextInt(6);
  @override
  _StandardProductCard createState() => _StandardProductCard();
}

class _StandardProductCard extends State<MyCard> {
  late IconData favoriteIcon ;
  late List<Product> favoriteProduct ;
  late bool isFavorite ;

    @override
      void initState() {
        favoriteProduct = CategoryService.getFavoriteProducts().toList();
        isFavorite = CategoryService.isFavorite(widget.product);

        if (isFavorite) {
            favoriteIcon = Icons.favorite;
         } else {
            favoriteIcon = Icons.favorite_border_outlined;
         }
      }

  void _favorite() {
    setState(() {
      isFavorite = !isFavorite;
      if (isFavorite) {
        favoriteIcon = Icons.favorite;
        CategoryService.addFavorite(widget.product);
      } else {
        CategoryService.removeFavorite(widget.product);
        widget.updateTheState?.call();
        favoriteIcon = Icons.favorite_border_outlined;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 260,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.15),
            blurRadius: 12,
            offset: const Offset(0, 6),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Stack(
            children: [
              ClipRRect(
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
                child: Image.network(
                  widget.product.imageUrl,
                  height: 180,
                  width: double.infinity,
                  fit: BoxFit.cover,
                ),
              ),
              Positioned(
                top: 12,
                right: 12,
                child: GestureDetector(
                  onTap: _favorite,
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      shape: BoxShape.circle,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.1),
                          blurRadius: 4,
                          offset: const Offset(0, 2),
                        ),
                      ],
                    ),
                    child: Icon(
                      favoriteIcon,
                      size: 20,
                      color: Colors.red,
                    ),
                  ),
                ),
              ),
            ],
          ),
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  widget.product.name,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                const SizedBox(height: 6),
                Row(
                  children: [
                    Row(
                      children: List.generate(5, (index) {
                        return Icon(
                          index < widget.rating.floor()
                              ? Icons.star
                              : (index < widget.rating
                                  ? Icons.star_half
                                  : Icons.star_border),
                          size: 14,
                          color: Colors.amber,
                        );
                      }),
                    ),
                    const SizedBox(width: 6),
                    Text(
                      widget.rating.toString(),
                      style: const TextStyle(
                        fontSize: 11,
                        color: Colors.grey,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      '\$${(widget.product.price - widget.product.price*widget.product.discount).toStringAsFixed(2)}',
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF4A90E2),
                      ),
                    ),
                    if (widget.product.discount != 0) ...[
                      const SizedBox(width: 8),
                      Text(
                        '\$${widget.product.price.toStringAsFixed(2)}',
                        style: const TextStyle(
                          fontSize: 13,
                          decoration: TextDecoration.lineThrough,
                          color: Colors.grey,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                      const SizedBox(width: 4),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                        decoration: BoxDecoration(
                          color: Colors.red.withOpacity(0.1),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          '${((1 - (widget.product.price - widget.product.price*widget.product.discount) / widget.product.price) * 100).toInt()}% OFF',
                          style: const TextStyle(
                            fontSize: 10,
                            color: Colors.red,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ],
                ),
                const SizedBox(height: 14),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () {},
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF4A90E2),
                      foregroundColor: Colors.white,
                      elevation: 0,
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                      ),
                      textStyle: const TextStyle(
                        fontSize: 13,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    child: const Text('Add to cart'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
