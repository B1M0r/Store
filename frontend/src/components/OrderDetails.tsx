import { Order } from '@/types';
import { Group } from 'lucide-react';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';

interface OrderDetailsProps {
    order: Order;
    open: boolean;
    onClose: () => void;
}

const OrderDetails = ({ order, open, onClose }: OrderDetailsProps) => {
    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="max-w-md">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold">Детали заказа #{order.id}</DialogTitle>
                    <DialogDescription>
                        Информация о заказе
                    </DialogDescription>
                </DialogHeader>

                <div className="space-y-6 py-4">
                    {/* Основная информация */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg">Основная информация</h3>
                        <div className="grid grid-cols-2 gap-2 text-sm">
                            <span className="text-gray-500">Дата заказа:</span>
                            <span>{new Date(order.orderDate).toLocaleString()}</span>
                            <span className="text-gray-500">Общая стоимость:</span>
                            <span className="font-semibold">{order.totalPrice} $</span>
                        </div>
                    </div>

                    {/* Many-to-Many: Связь с продуктами */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg flex items-center">
                            <Group className="h-5 w-5 mr-2 text-green-500" />
                            Товары
                        </h3>
                        {order.products && order.products.length > 0 ? (
                            <div className="space-y-2 max-h-64 overflow-y-auto">
                                {order.products.map(product => (
                                    <div
                                        key={product.id}
                                        className="bg-green-50 p-3 rounded-md border border-green-100 flex justify-between items-center"
                                    >
                                        <div>
                                            <div className="font-medium">{product.name}</div>
                                            <div className="text-sm text-gray-600">
                                                {product.category}
                                            </div>
                                        </div>
                                        <Badge variant="outline" className="bg-green-100">
                                            {product.price} $
                                        </Badge>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="text-gray-500 italic">В заказе нет товаров</div>
                        )}
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
};

export default OrderDetails;