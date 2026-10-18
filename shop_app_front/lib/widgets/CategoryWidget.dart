import 'package:flutter/material.dart';
import 'package:shop_app_front/api/service/catService.dart';
import 'package:shop_app_front/widgets/MyAppBar.dart';
import 'package:shop_app_front/widgets/ProductWidget.dart';

class CategoryWidget extends StatefulWidget {
    final String categoryName;
    final String imageUrl;
    final String? description;
    final CategoryService categoryService;

    const CategoryWidget({
        super.key,
        required this.categoryName,
        required this.imageUrl,
        this.description,
        required this.categoryService
    });

    @override
    _CategoryWidgetState createState() => _CategoryWidgetState();
}

class _CategoryWidgetState extends State<CategoryWidget> {

    bool isHovered = false;
    late int productCount;
    bool isLoading = true;

    @override
    Widget build(BuildContext context) {
        return GestureDetector(
            onTap: () {
                print("Category tapped: ${widget.categoryName}");
            },
            child: MouseRegion(
                onEnter: (_) => setState(() => isHovered = true),
                onExit: (_) => setState(() => isHovered = false),
                child: AnimatedContainer(
                    duration: const Duration(milliseconds: 200),
                    width: 260,
                    decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(20),
                        boxShadow: isHovered
                        ? [
                            BoxShadow(
                                color: Colors.grey.withOpacity(0.25),
                                blurRadius: 20,
                                offset: const Offset(0, 10),
                                spreadRadius: 2,
                            ),
                        ]
                        : [
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
                                            widget.imageUrl,
                                            height: 180,
                                            width: double.infinity,
                                            fit: BoxFit.cover,
                                            loadingBuilder: (context, child, loadingProgress) {
                                                if (loadingProgress == null) return child;
                                                return Container(
                                                    height: 180,
                                                    color: Colors.grey[100],
                                                    child: Center(
                                                        child: CircularProgressIndicator(
                                                            value: loadingProgress.expectedTotalBytes != null
                                                            ? loadingProgress.cumulativeBytesLoaded /
                                                            loadingProgress.expectedTotalBytes!
                                                            : null,
                                                            strokeWidth: 2,
                                                            valueColor: const AlwaysStoppedAnimation<Color>(
                                                                Color(0xFF4A90E2),
                                                            ),
                                                        ),
                                                    ),
                                                );
                                            },
                                            errorBuilder: (context, error, stackTrace) {
                                                return Container(
                                                    height: 180,
                                                    color: Colors.grey[100],
                                                    child: Column(
                                                        mainAxisAlignment: MainAxisAlignment.center,
                                                        children: [
                                                            Icon(
                                                                Icons.broken_image,
                                                                size: 48,
                                                                color: Colors.grey[400],
                                                            ),
                                                            const SizedBox(height: 8),
                                                            Text(
                                                                'Image not available',
                                                                style: TextStyle(
                                                                    fontSize: 12,
                                                                    color: Colors.grey[500],
                                                                ),
                                                            ),
                                                        ],
                                                    ),
                                                );
                                            },
                                        ),
                                    ),

                                    Positioned(
                                        top: 12,
                                        left: 12,
                                        child: Container(
                                            padding: const EdgeInsets.symmetric(
                                                horizontal: 10,
                                                vertical: 5,
                                            ),
                                            decoration: BoxDecoration(
                                                gradient: LinearGradient(
                                                    colors: [
                                                        Colors.black.withOpacity(0.7),
                                                        Colors.black.withOpacity(0.5),
                                                    ],
                                                ),
                                                borderRadius: BorderRadius.circular(20),
                                                border: Border.all(
                                                    color: Colors.white.withOpacity(0.3),
                                                    width: 0.5,
                                                ),
                                            ),
                                            child: Row(
                                                mainAxisSize: MainAxisSize.min,
                                                children: [
                                                    Icon(
                                                        _getCategoryIcon(widget.categoryName),
                                                        size: 14,
                                                        color: Colors.white,
                                                    ),
                                                    const SizedBox(width: 5),
                                                    Text(
                                                        widget.categoryName,
                                                        style: const TextStyle(
                                                            fontSize: 11,
                                                            color: Colors.white,
                                                            fontWeight: FontWeight.w600,
                                                        ),
                                                    ),
                                                ],
                                            ),
                                        ),
                                    ),

                                    Positioned(
                                        bottom: 12,
                                        right: 12,
                                        child: Container(
                                            padding: const EdgeInsets.symmetric(
                                                horizontal: 8,
                                                vertical: 4,
                                            ),
                                            decoration: BoxDecoration(
                                                color: Colors.black.withOpacity(0.6),
                                                borderRadius: BorderRadius.circular(12),
                                            ),
                                            child: isLoading
                                            ? const SizedBox(
                                                width: 50,
                                                height: 16,
                                                child: Center(
                                                    child: SizedBox(
                                                        width: 12,
                                                        height: 12,
                                                        child: CircularProgressIndicator(
                                                            strokeWidth: 2,
                                                            color: Colors.white,
                                                        ),
                                                    ),
                                                ),
                                            )
                                            : Row(
                                                children: [
                                                    const Icon(
                                                        Icons.shopping_bag,
                                                        size: 12,
                                                        color: Colors.white,
                                                    ),
                                                    const SizedBox(width: 4),
                                                    Text(
                                                        '$productCount items',
                                                        style: const TextStyle(
                                                            fontSize: 10,
                                                            color: Colors.white,
                                                        ),
                                                    ),
                                                ],
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
                                        Row(
                                            children: [
                                                Expanded(
                                                    child: Text(
                                                        widget.categoryName,
                                                        style: const TextStyle(
                                                            fontSize: 16,
                                                            fontWeight: FontWeight.bold,
                                                            color: Color(0xFF2C3E50),
                                                            letterSpacing: 0.5,
                                                        ),
                                                        maxLines: 1,
                                                        overflow: TextOverflow.ellipsis,
                                                    ),
                                                ),
                                                Icon(
                                                    Icons.arrow_forward_ios,
                                                    size: 12,
                                                    color: Colors.grey[400],
                                                ),
                                            ],
                                        ),

                                        const SizedBox(height: 6),

                                        if (widget.description != null && widget.description!.isNotEmpty)
                                        Column(
                                            children: [
                                                Text(
                                                    widget.description!,
                                                    style: TextStyle(
                                                        fontSize: 12,
                                                        color: Colors.grey[600],
                                                        height: 1.3,
                                                    ),
                                                    maxLines: 2,
                                                    overflow: TextOverflow.ellipsis,
                                                ),
                                                const SizedBox(height: 8),
                                            ],
                                        ),
                                    ],
                                ),
                            ),

                            Container(
                                margin: const EdgeInsets.fromLTRB(12, 0, 12, 12),
                                child: ElevatedButton(
                                    onPressed: () {
                                        Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) => Scaffold(
                                                    appBar: MyAppBar(title: widget.categoryName),
                                                    body: MyProductWidget(
                                                        categoryService: widget.categoryService,
                                                        categoryName: widget.categoryName,
                                                    ),
                                                ),
                                            ),
                                        );
                                    },
                                    style: ElevatedButton.styleFrom(
                                        backgroundColor: const Color(0xFF4A90E2),
                                        foregroundColor: Colors.white,
                                        elevation: 0,
                                        shape: RoundedRectangleBorder(
                                            borderRadius: BorderRadius.circular(12),
                                        ),
                                        minimumSize: const Size(double.infinity, 36),
                                        textStyle: const TextStyle(
                                            fontSize: 13,
                                            fontWeight: FontWeight.w600,
                                        ),
                                    ),
                                    child: const Text('Browse Now'),
                                ),
                            ),
                        ],
                    ),
                ),
            ),
        );
    }

    @override
    void initState() {
        super.initState();
        _loadProductCount();
    }

    IconData _getCategoryIcon(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case 'electronics':
                return Icons.electrical_services;
            case 'clothing':
                return Icons.checkroom;
            case 'books':
                return Icons.menu_book;
            case 'sports':
                return Icons.sports_soccer;
            case 'toys':
                return Icons.toys;
            default:
                return Icons.category;
        }
    }

    Future<void> _loadProductCount() async {
        try {
            final count = await widget.categoryService.getProductCount(widget.categoryName);
            if (mounted) {
        setState(() {
          productCount = count;
          isLoading = false;
        });
      }
        } catch (e) {
            if (mounted) {
        setState(() {
          productCount = 0;
          isLoading = false;
        });
      }
            print('Error loading product count: $e');
        }finally{
            setState(() {
                productCount = productCount??0;
                isLoading = false;
            });
        }
    }
}
