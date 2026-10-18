import 'package:flutter/material.dart';
import 'package:shop_app_front/widgets/favoritProductsWidget.dart';

class MyAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String title;

  const MyAppBar({super.key, required this.title});

  @override
  Widget build(BuildContext context) {
    return AppBar(
      title: Text(title),
      backgroundColor: const Color(0xFF4A90E2),
      foregroundColor: Colors.white,
      elevation: 0,
      actions: [
        IconButton(
          icon: const Icon(Icons.home),
          onPressed: () {
            Navigator.popUntil(context, (route) => route.isFirst);
          },
                ),
                IconButton(
                    icon: const Icon(Icons.favorite_border),
                    onPressed: () {
                        Navigator.push(context, MaterialPageRoute(
                            builder: (context) => Scaffold(
                                appBar: MyAppBar(title: "Favorits"),
                                body: FavoriteProductsWidget(),
                                ),
                            )
                        );

                    },
                ),
      ],
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
