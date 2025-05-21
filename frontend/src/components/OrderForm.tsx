import { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Order, Product, Account } from '@/types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { fetchAccounts } from '@/services/api';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { Checkbox } from '@/components/ui/checkbox';

interface OrderFormProps {
    order: Order | null;
    onSubmit: (order: Order) => void;
    products: Product[];
    isEditMode: boolean;
}

const OrderForm = ({ order, onSubmit, products, isEditMode }: OrderFormProps) => {
    const [totalPrice, setTotalPrice] = useState<number>(order?.totalPrice || 0);
    const [selectedAccountId, setSelectedAccountId] = useState<string>('');
    const [selectedProductIds, setSelectedProductIds] = useState<number[]>([]);

    const { data: accounts = [] } = useQuery({
        queryKey: ['accounts'],
        queryFn: fetchAccounts,
    });

    useEffect(() => {
        if (order) {
            setTotalPrice(order.totalPrice);
            setSelectedAccountId(order.account?.id?.toString() || '');
            setSelectedProductIds(order.products?.map(p => p.id || 0).filter(id => id !== 0) || []);
        } else {
            setTotalPrice(0);
            setSelectedAccountId('');
            setSelectedProductIds([]);
        }
    }, [order]);

    // Автоматически рассчитываем общую стоимость на основе выбранных продуктов
    useEffect(() => {
        if (selectedProductIds.length > 0) {
            const sum = products
                .filter(p => selectedProductIds.includes(p.id || 0))
                .reduce((acc, p) => acc + p.price, 0);
            setTotalPrice(sum);
        } else {
            setTotalPrice(0);
        }
    }, [selectedProductIds, products]);

    const toggleProductSelection = (productId: number) => {
        setSelectedProductIds(prev =>
            prev.includes(productId)
                ? prev.filter(id => id !== productId)
                : [...prev, productId]
        );
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        var orderData: Order = {
            id: order?.id,
            orderDate: order?.orderDate || new Date().toISOString(),
            totalPrice,
            productIds: selectedProductIds,
            account: accounts.find(acc => acc.id === Number(selectedAccountId))
        };
        orderData.account.orders = [];

        onSubmit(orderData);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 pt-4">
            <div className="space-y-2">
                <Label htmlFor="account">Аккаунт</Label>
                <Select
                    value={selectedAccountId}
                    onValueChange={setSelectedAccountId}
                    required
                >
                    <SelectTrigger id="account">
                        <SelectValue placeholder="Выберите аккаунт" />
                    </SelectTrigger>
                    <SelectContent>
                        {accounts.map((account) => (
                            <SelectItem key={account.id} value={account.id?.toString() || ''}>
                                {account.firstName} {account.lastName}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
            </div>

            <div className="space-y-2">
                <Label htmlFor="products">Продукты</Label>
                <div className="max-h-40 overflow-y-auto border rounded-md p-2 space-y-2">
                    {products.map((product) => (
                        <div key={product.id} className="flex items-center space-x-2">
                            <input
                                type="checkbox"
                                id={`product-${product.id}`}
                                checked={selectedProductIds.includes(product.id || 0)}
                                onChange={() => toggleProductSelection(product.id || 0)}
                                className="h-4 w-4 rounded border-gray-300"
                            />
                            <label htmlFor={`product-${product.id}`} className="flex-1">
                                {product.name} - {product.price} $
                            </label>
                        </div>
                    ))}
                    {products.length === 0 && (
                        <div className="text-center py-2 text-gray-500">Нет доступных продуктов</div>
                    )}
                </div>
            </div>

            <div className="space-y-2">
                <Label htmlFor="totalPrice">Общая стоимость</Label>
                <Input
                    id="totalPrice"
                    type="number"
                    value={totalPrice}
                    onChange={(e) => setTotalPrice(Number(e.target.value))}
                    readOnly
                    className="bg-gray-50"
                    required
                />
            </div>

            <div className="flex justify-end space-x-2 pt-2">
                <Button type="submit" className="bg-orange-500 hover:bg-orange-600">
                    {isEditMode ? 'Сохранить' : 'Создать'}
                </Button>
            </div>
        </form>
    );
};

export default OrderForm;
