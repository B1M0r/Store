
import { useState } from 'react';
import { Product } from '@/types';
import { Group } from 'lucide-react';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';

interface ProductDetailsProps {
    product: Product;
    open: boolean;
    onClose: () => void;
}

const ProductDetails = ({ product, open, onClose }: ProductDetailsProps) => {
    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="max-w-md">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold">Детали товара: {product.name}</DialogTitle>
                    <DialogDescription>
                        Подробная информация о товаре
                    </DialogDescription>
                </DialogHeader>

                <div className="space-y-6 py-4">
                    {/* Основная информация */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg">Основная информация</h3>
                        <div className="grid grid-cols-2 gap-2 text-sm">
                            <span className="text-gray-500">Название:</span>
                            <span>{product.name}</span>
                            <span className="text-gray-500">Категория:</span>
                            <span>{product.category}</span>
                            <span className="text-gray-500">Цена:</span>
                            <span>{product.price} $</span>
                        </div>
                    </div>

                    {/* Many-to-Many: Связь с заказами */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg flex items-center">
                            <Group className="h-5 w-5 mr-2 text-orange-500" />
                            Заказы
                        </h3>
                        {product.orders && product.orders.length > 0 ? (
                            <div className="space-y-2">
                                {product.orders.map(order => (
                                    <div
                                        key={order.id}
                                        className="bg-orange-50 p-3 rounded-md border border-orange-100 flex justify-between items-center"
                                    >
                                        <div>
                                            <div className="font-medium">Заказ #{order.id}</div>
                                            <div className="text-sm text-gray-600">
                                                {new Date(order.orderDate).toLocaleDateString()}
                                            </div>
                                        </div>
                                        <Badge variant="outline" className="bg-orange-100">
                                            {order.totalPrice} $
                                        </Badge>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="text-gray-500 italic">Товар не включен ни в один заказ</div>
                        )}
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
};

export default ProductDetails;
