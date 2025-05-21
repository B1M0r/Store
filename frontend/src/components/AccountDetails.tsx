
import { Account } from '@/types';
import { Layers, Group } from 'lucide-react';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import { ScrollArea } from '@/components/ui/scroll-area';

interface AccountDetailsProps {
    account: Account;
    open: boolean;
    onClose: () => void;
}

const AccountDetails = ({ account, open, onClose }: AccountDetailsProps) => {
    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="max-w-md">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold">Детали аккаунта: {account.firstName} {account.lastName}</DialogTitle>
                    <DialogDescription>
                        Информация об аккаунте
                    </DialogDescription>
                </DialogHeader>

                <div className="space-y-6 py-4">
                    {/* Основная информация */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg">Основная информация</h3>
                        <div className="grid grid-cols-2 gap-2 text-sm">
                            <span className="text-gray-500">Никнейм:</span>
                            <span>{account.nickname}</span>
                            <span className="text-gray-500">Имя:</span>
                            <span>{account.firstName}</span>
                            <span className="text-gray-500">Фамилия:</span>
                            <span>{account.lastName}</span>
                            <span className="text-gray-500">Email:</span>
                            <span>{account.email}</span>
                        </div>
                    </div>

                    {/* One-to-Many: Связь с заказами */}
                    <div className="space-y-2">
                        <h3 className="font-semibold text-lg flex items-center">
                            <Group className="h-5 w-5 mr-2 text-orange-500" />
                            Заказы
                        </h3>
                        {account.orders && account.orders.length > 0 ? (
                            <ScrollArea className="h-64">
                                <div className="space-y-4">
                                    {account.orders.map(order => (
                                        <div key={order.id} className="space-y-2">
                                            <div className="bg-orange-50 p-3 rounded-md border border-orange-100 flex justify-between items-center">
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

                                            {/* Товары в заказе */}
                                            {order.products && order.products.length > 0 ? (
                                                <div className="ml-4 space-y-2">
                                                    <div className="text-sm font-medium text-gray-600">Товары в заказе:</div>
                                                    {order.products.map(product => (
                                                        <div
                                                            key={product.id}
                                                            className="bg-green-50 p-2 rounded-md border border-green-100 flex justify-between items-center"
                                                        >
                                                            <div>
                                                                <div className="text-sm font-medium">{product.name}</div>
                                                                <div className="text-xs text-gray-600">
                                                                    {product.category}
                                                                </div>
                                                            </div>
                                                            <Badge variant="outline" className="bg-green-100 text-xs">
                                                                {product.price} $
                                                            </Badge>
                                                        </div>
                                                    ))}
                                                </div>
                                            ) : (
                                                <div className="ml-4 text-xs text-gray-500 italic">В заказе нет товаров</div>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            </ScrollArea>
                        ) : (
                            <div className="text-gray-500 italic">Нет связанных заказов</div>
                        )}
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
};

export default AccountDetails;